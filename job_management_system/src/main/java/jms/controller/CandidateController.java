package jms.controller;

import jms.dto.CandidateCreateDTO;
import jms.entity.CandidateProfile;
import jms.entity.Skill;
import jms.entity.Document; // THÊM IMPORT NÀY
import jms.service.CandidateProfileService;
import jms.service.SkillService;
import jms.service.DocumentService;
import jms.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile; // THÊM IMPORT NÀY

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors; // THÊM IMPORT NÀY

@Controller
@RequestMapping("/candidates")
@Slf4j
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateProfileService candidateProfileService;
    private final SkillService skillService;
    private final DocumentService documentService;
    private final ApplicationService applicationService;

    @GetMapping
    public String listCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Integer minExp,
            @RequestParam(required = false) Integer maxExp,
            Model model) {

        log.info("=== CANDIDATE CONTROLLER CALLED ===");
        log.info("Page: {}, Size: {}, Search: '{}', Skill: '{}', MinExp: {}, MaxExp: {}",
                page, size, search, skill, minExp, maxExp);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CandidateProfile> candidatePage;

        log.info("Search parameters - search: {}, skill: {}, minExp: {}, maxExp: {}", search, skill, minExp, maxExp);

        if (search != null && !search.trim().isEmpty()) {
            List<CandidateProfile> candidates = candidateProfileService.searchByKeyword(search.trim());
            log.info("Found {} candidates by search: {}", candidates.size(), search);

            // QUAN TRỌNG: Force initialize skills
            candidates.forEach(c -> {
                if (c.getSkills() != null) c.getSkills().size();
            });

            candidatePage = createPageFromList(candidates, pageable);
        } else if (skill != null && !skill.trim().isEmpty()) {
            List<CandidateProfile> candidates = candidateProfileService.findBySkillNames(List.of(skill.trim()));
            log.info("Found {} candidates by skill: {}", candidates.size(), skill);

            // QUAN TRỌNG: Force initialize skills
            candidates.forEach(c -> {
                if (c.getSkills() != null) c.getSkills().size();
            });

            candidatePage = createPageFromList(candidates, pageable);
        } else if (minExp != null || maxExp != null) {
            int min = minExp != null ? minExp : 0;
            int max = maxExp != null ? maxExp : 50;
            List<CandidateProfile> candidates = candidateProfileService.findByExperienceRange(min, max);
            log.info("Found {} candidates by experience range: {}-{}", candidates.size(), min, max);

            // QUAN TRỌNG: Force initialize skills
            candidates.forEach(c -> {
                if (c.getSkills() != null) c.getSkills().size();
            });

            candidatePage = createPageFromList(candidates, pageable);
        } else {
            candidatePage = candidateProfileService.getCandidateProfiles(pageable);
            log.info("Found {} candidates total (page: {})", candidatePage.getTotalElements(), page);

            // QUAN TRỌNG: Force initialize skills for each candidate
            candidatePage.getContent().forEach(c -> {
                if (c.getSkills() != null) c.getSkills().size();
            });
        }

        List<Skill> allSkills = skillService.getAllSkills();
        log.info("Loaded {} skills for filter", allSkills.size());

        model.addAttribute("candidates", candidatePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("skill", skill);
        model.addAttribute("minExp", minExp);
        model.addAttribute("maxExp", maxExp);
        model.addAttribute("allSkills", allSkills);
        model.addAttribute("currentPath", "/candidates");

        return "candidates/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Skill> allSkills = skillService.getAllSkills();
        // Đảm bảo tạo đúng instance của CandidateCreateDTO
        CandidateCreateDTO candidateDTO = new CandidateCreateDTO();
        candidateDTO.setExperienceYears(0); // Set giá trị mặc định nếu cần

        model.addAttribute("candidateDTO", candidateDTO);
        model.addAttribute("allSkills", allSkills);
        model.addAttribute("currentPath", "/candidates");
        return "candidates/create";
    }

    @PostMapping("/create")
    public String createCandidate(@ModelAttribute("candidateDTO") CandidateCreateDTO candidateDTO,
                                  @RequestParam(required = false) List<MultipartFile> documents,
                                  @RequestParam(required = false) List<String> documentNames,
                                  RedirectAttributes redirectAttributes) {

        try {
            log.info("Creating candidate with email: {}", candidateDTO.getEmail());
            log.info("Skills: {}", candidateDTO.getSkillIds());

            // Convert DTO to Entity
            CandidateProfile candidate = CandidateProfile.builder()
                    .fullName(candidateDTO.getFullName() != null ? candidateDTO.getFullName().trim() : null)
                    .email(candidateDTO.getEmail() != null ? candidateDTO.getEmail().trim() : null)
                    .phone(candidateDTO.getPhone() != null ? candidateDTO.getPhone().trim() : null)
                    .careerGoal(candidateDTO.getCareerGoal() != null ? candidateDTO.getCareerGoal().trim() : null)
                    .education(candidateDTO.getEducation() != null ? candidateDTO.getEducation().trim() : null)
                    .experienceYears(candidateDTO.getExperienceYears() != null ? candidateDTO.getExperienceYears() : 0)
                    .previousCompany(candidateDTO.getPreviousCompany() != null ? candidateDTO.getPreviousCompany().trim() : null)
                    .certificates(candidateDTO.getCertificates() != null ? candidateDTO.getCertificates().trim() : null)
                    .skills(new HashSet<>())
                    .build();

            // Tạo candidate profile (validation sẽ được thực hiện trong service)
            CandidateProfile savedCandidate = candidateProfileService.createCandidateProfile(candidate, candidateDTO.getSkillIds());

            // Xử lý upload documents nếu có
            if (documents != null && documentNames != null && !documents.isEmpty() && documents.size() == documentNames.size()) {
                for (int i = 0; i < documents.size(); i++) {
                    MultipartFile file = documents.get(i);
                    String documentName = documentNames.get(i);

                    if (file != null && !file.isEmpty() && documentName != null && !documentName.trim().isEmpty()) {
                        try {
                            documentService.uploadDocumentForCandidate(savedCandidate.getId(), file, documentName.trim());
                        } catch (Exception e) {
                            log.warn("Failed to upload document {} for candidate: {}", documentName, e.getMessage());
                        }
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Tạo hồ sơ ứng viên thành công!");
            return "redirect:/candidates";

        } catch (Exception e) {
            log.error("Error creating candidate", e);

            // Hiển thị lỗi validation từ service
            String errorMessage = "Lỗi khi tạo hồ sơ: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

            // Giữ lại dữ liệu đã nhập và quay lại form
            redirectAttributes.addFlashAttribute("candidateDTO", candidateDTO);
            return "redirect:/candidates/create";
        }
    }


    @GetMapping("/{id}")
    public String viewCandidate(@PathVariable Long id, Model model) {
        log.info("=== VIEW CANDIDATE DETAIL - ID: {} ===", id);

        Optional<CandidateProfile> candidateOptional = candidateProfileService.getCandidateProfileById(id);

        if (candidateOptional.isPresent()) {
            CandidateProfile candidate = candidateOptional.get();
            try {
                // QUAN TRỌNG: Force initialize các relationships
                if (candidate.getSkills() != null) {
                    candidate.getSkills().size(); // Force initialize skills
                }

                // QUAN TRỌNG: Force initialize documents relationship nếu có
                if (candidate.getDocuments() != null) {
                    candidate.getDocuments().size();
                }

                // Lấy danh sách documents của candidate từ service
                List<Document> documents = documentService.getDocumentsByCandidateId(id);
                log.info("Found {} documents for candidate ID: {}", documents.size(), id);

                // Debug: in thông tin từng document
                documents.forEach(doc -> {
                    log.info("Document: ID={}, Name={}, FileName={}, Type={}, Size={} bytes",
                            doc.getId(), doc.getDocumentName(), doc.getFileName(),
                            doc.getFileType(), doc.getFileData() != null ? doc.getFileData().length : 0);
                });

                // Set documents vào candidate (QUAN TRỌNG)
                candidate.setDocuments(new HashSet<>(documents));

                // Đếm số lượng documents
                int documentCount = documents.size();

                // Lấy số lượng đơn ứng tuyển
                Long applicationCount = 0L;
                try {
                    applicationCount = applicationService.countApplicationsByCandidateId(id);
                } catch (Exception e) {
                    log.warn("Could not get application count: {}", e.getMessage());
                }

                model.addAttribute("candidate", candidate);
                model.addAttribute("applicationCount", applicationCount);
                model.addAttribute("documentCount", documentCount);
                model.addAttribute("currentPath", "/candidates");

                log.info("Added to model - candidate: {}, documents: {}, applicationCount: {}",
                        candidate.getFullName(), documentCount, applicationCount);

                return "candidates/detail";
            } catch (Exception e) {
                log.error("Error processing candidate details for ID: {}", id, e);
                model.addAttribute("errorMessage", "Lỗi khi tải thông tin ứng viên: " + e.getMessage());
                return "candidates/detail";
            }
        } else {
            log.error("Candidate not found with ID: {}", id);
            return "redirect:/candidates";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return candidateProfileService.getCandidateProfileById(id)
                .map(candidate -> {
                    List<Skill> allSkills = skillService.getAllSkills();
                    model.addAttribute("candidate", candidate);
                    model.addAttribute("allSkills", allSkills);
                    model.addAttribute("currentPath", "/candidates");
                    return "candidates/edit";
                })
                .orElse("redirect:/candidates");
    }

    @PostMapping("/{id}/edit")
    public String updateCandidate(@PathVariable Long id,
                                  @RequestParam String fullName,
                                  @RequestParam(required = false) String phone,
                                  @RequestParam(required = false) String education,
                                  @RequestParam(required = false) Integer experienceYears,
                                  @RequestParam(required = false) String previousCompany,
                                  @RequestParam(required = false) String careerGoal,
                                  @RequestParam(required = false) String certificates,
                                  @RequestParam(required = false) List<Long> skillIds,
                                  @RequestParam(required = false) List<MultipartFile> newDocuments,
                                  @RequestParam(required = false) List<String> newDocumentNames,
                                  @RequestParam(required = false) List<Long> deleteDocumentIds,
                                  RedirectAttributes redirectAttributes) {

        log.info("=== UPDATE CANDIDATE CALLED ===");
        log.info("Candidate ID: {}", id);

        try {
            // Lấy candidate hiện tại
            CandidateProfile candidate = candidateProfileService.getCandidateProfileById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ứng viên"));

            // Cập nhật thông tin cơ bản
            candidate.setFullName(fullName);
            candidate.setPhone(phone);
            candidate.setEducation(education);
            candidate.setExperienceYears(experienceYears != null ? experienceYears : 0);
            candidate.setPreviousCompany(previousCompany);
            candidate.setCareerGoal(careerGoal);
            candidate.setCertificates(certificates);

            // Cập nhật candidate (validation sẽ được thực hiện trong service)
            candidateProfileService.updateCandidateProfile(id, candidate);

            // Update skills
            updateCandidateSkills(id, skillIds);

            // Xử lý documents
            processDocuments(id, deleteDocumentIds, newDocuments, newDocumentNames);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ ứng viên thành công!");
            return "redirect:/candidates/" + id;

        } catch (Exception e) {
            log.error(" ERROR updating candidate: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
            return "redirect:/candidates/" + id + "/edit";
        }
    }

    private void updateCandidateSkills(Long candidateId, List<Long> skillIds) {
        try {
            log.info("Updating skills for candidate ID: {}, Skills: {}", candidateId, skillIds);

            CandidateProfile candidate = candidateProfileService.getCandidateProfileById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            // Force initialize skills để đảm bảo có dữ liệu mới nhất
            if (candidate.getSkills() != null) {
                candidate.getSkills().size();
            }

            List<Long> currentSkillIds = candidate.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());

            log.info("Current skills: {}, New skills: {}", currentSkillIds, skillIds);

            if (skillIds != null && !skillIds.isEmpty()) {
                // Remove skills not in new list
                List<Long> skillsToRemove = currentSkillIds.stream()
                        .filter(skillId -> !skillIds.contains(skillId))
                        .collect(Collectors.toList());

                if (!skillsToRemove.isEmpty()) {
                    log.info("Removing skills: {}", skillsToRemove);
                    candidateProfileService.removeSkillsFromCandidate(candidateId, skillsToRemove);
                }

                // Add new skills
                List<Long> skillsToAdd = skillIds.stream()
                        .filter(skillId -> !currentSkillIds.contains(skillId))
                        .collect(Collectors.toList());

                if (!skillsToAdd.isEmpty()) {
                    log.info("Adding skills: {}", skillsToAdd);
                    candidateProfileService.addSkillsToCandidate(candidateId, skillsToAdd);
                }
            } else {
                // Remove all skills if no skills selected
                if (!currentSkillIds.isEmpty()) {
                    log.info("Removing all skills: {}", currentSkillIds);
                    candidateProfileService.removeSkillsFromCandidate(candidateId, currentSkillIds);
                }
            }

            log.info("✅ Skills updated successfully for candidate ID: {}", candidateId);
        } catch (Exception e) {
            log.error(" Error updating skills for candidate ID: {} - {}", candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to update skills: " + e.getMessage(), e);
        }
    }

    private void processDocuments(Long candidateId, List<Long> deleteDocumentIds,
                                  List<MultipartFile> newDocuments, List<String> newDocumentNames) {
        try {
            // Xóa documents
            if (deleteDocumentIds != null && !deleteDocumentIds.isEmpty()) {
                log.info("Deleting {} documents: {}", deleteDocumentIds.size(), deleteDocumentIds);
                for (Long docId : deleteDocumentIds) {
                    try {
                        documentService.deleteDocument(docId);
                        log.info("✅ Deleted document ID: {}", docId);
                    } catch (Exception e) {
                        log.error(" Failed to delete document {}: {}", docId, e.getMessage());
                        // Không throw exception để tiếp tục xử lý các document khác
                    }
                }
            }

            // Thêm documents mới
            if (newDocuments != null && !newDocuments.isEmpty()) {
                log.info("Processing {} new documents", newDocuments.size());

                for (int i = 0; i < newDocuments.size(); i++) {
                    MultipartFile file = newDocuments.get(i);
                    if (file != null && !file.isEmpty()) {
                        String documentName = getDocumentName(newDocumentNames, i, file);

                        log.info("Uploading document {}: {} (size: {} bytes, type: {})",
                                i, documentName, file.getSize(), file.getContentType());

                        try {
                            // Kiểm tra kích thước file trước khi upload
                            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                                log.warn("⚠️ File too large: {} - {} bytes", documentName, file.getSize());
                                continue; // Bỏ qua file quá lớn
                            }

                            Document savedDoc = documentService.uploadDocumentForCandidate(candidateId, file, documentName);
                            log.info("✅ SUCCESS: Uploaded document '{}' with ID: {}, Size: {} bytes",
                                    documentName, savedDoc.getId(),
                                    savedDoc.getFileData() != null ? savedDoc.getFileData().length : 0);
                        } catch (Exception e) {
                            log.error(" FAILED: Upload document '{}' - {}", documentName, e.getMessage());
                            // Không throw exception để tiếp tục xử lý các document khác
                        }
                    } else {
                        log.warn("⚠️ Skipping empty document at index: {}", i);
                    }
                }
            }

            log.info("✅ Documents processing completed for candidate ID: {}", candidateId);
        } catch (Exception e) {
            log.error(" Error processing documents for candidate ID: {} - {}", candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to process documents: " + e.getMessage(), e);
        }
    }

    private String getDocumentName(List<String> documentNames, int index, MultipartFile file) {
        try {
            // Ưu tiên sử dụng tên từ input text
            if (documentNames != null && index < documentNames.size() &&
                    documentNames.get(index) != null && !documentNames.get(index).trim().isEmpty()) {
                return documentNames.get(index).trim();
            }

            // Fallback: sử dụng tên file gốc
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().trim().isEmpty()) {
                // Loại bỏ extension để có tên đẹp hơn
                String originalName = file.getOriginalFilename();
                int lastDotIndex = originalName.lastIndexOf('.');
                if (lastDotIndex > 0) {
                    return originalName.substring(0, lastDotIndex);
                }
                return originalName;
            }

            // Fallback cuối cùng
            return "Document " + (index + 1) + " - " +
                    (file.getContentType() != null ?
                            file.getContentType().split("/")[1] : "file");

        } catch (Exception e) {
            log.warn("Error getting document name, using default: {}", e.getMessage());
            return "Document " + (index + 1);
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCandidate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateProfileService.deleteCandidateProfile(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa hồ sơ ứng viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa hồ sơ: " + e.getMessage());
        }
        return "redirect:/candidates";
    }

    // THÊM PHƯƠNG THỨC NÀY VÀO CUỐI CLASS, TRƯỚC DẤU }
    private Page<CandidateProfile> createPageFromList(List<CandidateProfile> candidates, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), candidates.size());

        if (start > candidates.size()) {
            return new PageImpl<>(List.of(), pageable, candidates.size());
        }

        return new PageImpl<>(candidates.subList(start, end), pageable, candidates.size());
    }
}