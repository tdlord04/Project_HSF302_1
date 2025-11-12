package jms.controller;

import jms.entity.CandidateProfile;
import jms.entity.Skill;
import jms.service.CandidateProfileService;
import jms.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidates")
@Slf4j
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateProfileService candidateProfileService;
    private final SkillService skillService;

    @GetMapping
    public String listCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Integer minExp,
            @RequestParam(required = false) Integer maxExp,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CandidateProfile> candidatePage;

        if (search != null && !search.trim().isEmpty()) {
            List<CandidateProfile> candidates = candidateProfileService.searchByKeyword(search.trim());
            candidatePage = createPageFromList(candidates, pageable);
        } else if (skill != null && !skill.trim().isEmpty()) {
            List<CandidateProfile> candidates = candidateProfileService.findBySkillNames(List.of(skill.trim()));
            candidatePage = createPageFromList(candidates, pageable);
        } else if (minExp != null || maxExp != null) {
            int min = minExp != null ? minExp : 0;
            int max = maxExp != null ? maxExp : 50;
            List<CandidateProfile> candidates = candidateProfileService.findByExperienceRange(min, max);
            candidatePage = createPageFromList(candidates, pageable);
        } else {
            candidatePage = candidateProfileService.getCandidateProfiles(pageable);
        }

        List<Skill> allSkills = skillService.getAllSkills();

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

    // Phương thức hỗ trợ tạo Page từ List
    private Page<CandidateProfile> createPageFromList(List<CandidateProfile> candidates, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), candidates.size());

        if (start > candidates.size()) {
            return new PageImpl<>(List.of(), pageable, candidates.size());
        }

        return new PageImpl<>(candidates.subList(start, end), pageable, candidates.size());
    }

    // Các phương thức khác giữ nguyên...
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Skill> allSkills = skillService.getAllSkills();
        model.addAttribute("candidate", new CandidateProfile());
        model.addAttribute("allSkills", allSkills);
        model.addAttribute("currentPath", "/candidates");
        return "candidates/create";
    }

    @PostMapping("/create")
    public String createCandidate(@ModelAttribute CandidateProfile candidate,
                                  @RequestParam(required = false) List<Long> skillIds,
                                  RedirectAttributes redirectAttributes) {
        try {
            CandidateProfile savedCandidate = candidateProfileService.createCandidateProfile(candidate);

            if (skillIds != null && !skillIds.isEmpty()) {
                candidateProfileService.addSkillsToCandidate(savedCandidate.getId(), skillIds);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Tạo hồ sơ ứng viên thành công!");
            return "redirect:/candidates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo hồ sơ: " + e.getMessage());
            return "redirect:/candidates/create";
        }
    }

    @GetMapping("/{id}")
    public String viewCandidate(@PathVariable Long id, Model model) {
        return candidateProfileService.getCandidateProfileById(id)
                .map(candidate -> {
                    model.addAttribute("candidate", candidate);
                    model.addAttribute("currentPath", "/candidates");
                    return "candidates/detail";
                })
                .orElse("redirect:/candidates");
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
                                  @ModelAttribute CandidateProfile candidate,
                                  @RequestParam(required = false) List<Long> skillIds,
                                  RedirectAttributes redirectAttributes) {
        try {
            CandidateProfile updatedCandidate = candidateProfileService.updateCandidateProfile(id, candidate);

            // Update skills
            CandidateProfile existingCandidate = candidateProfileService.getCandidateProfileById(id).orElseThrow();
            List<Long> existingSkillIds = existingCandidate.getSkills().stream()
                    .map(Skill::getId)
                    .collect(Collectors.toList());

            if (skillIds != null) {
                // Remove skills not in new list
                List<Long> skillsToRemove = existingSkillIds.stream()
                        .filter(skillId -> !skillIds.contains(skillId))
                        .collect(Collectors.toList());
                if (!skillsToRemove.isEmpty()) {
                    candidateProfileService.removeSkillsFromCandidate(id, skillsToRemove);
                }

                // Add new skills
                List<Long> skillsToAdd = skillIds.stream()
                        .filter(skillId -> !existingSkillIds.contains(skillId))
                        .collect(Collectors.toList());
                if (!skillsToAdd.isEmpty()) {
                    candidateProfileService.addSkillsToCandidate(id, skillsToAdd);
                }
            } else {
                // Remove all skills if no skills selected
                candidateProfileService.removeSkillsFromCandidate(id, existingSkillIds);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ ứng viên thành công!");
            return "redirect:/candidates/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
            return "redirect:/candidates/" + id + "/edit";
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
}