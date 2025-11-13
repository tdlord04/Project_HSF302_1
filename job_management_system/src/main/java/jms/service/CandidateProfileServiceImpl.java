// CandidateProfileServiceImpl.java
package jms.service.impl;

import jms.entity.CandidateProfile;
import jms.entity.Skill;
import jms.repository.CandidateProfileRepository;
import jms.repository.SkillRepository;
import jms.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final SkillRepository skillRepository;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]{2,255}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final List<String> VALID_EDUCATION_LEVELS = List.of("Trung học", "Cao đẳng", "Đại học");

    @Override
    public CandidateProfile createCandidateProfile(CandidateProfile candidateProfile, List<Long> skillIds) {
        log.info("Creating candidate profile for email: {}", candidateProfile.getEmail());

        // Validate input data
        validateCandidateData(candidateProfile);

        // Validate skills
        validateSkills(skillIds);

        if (candidateProfileRepository.existsByEmail(candidateProfile.getEmail())) {
            throw new RuntimeException("Ứng viên với email " + candidateProfile.getEmail() + " đã tồn tại trong hệ thống");
        }

        // ĐẢM BẢO skills KHÔNG NULL
        if (candidateProfile.getSkills() == null) {
            candidateProfile.setSkills(new HashSet<>());
        }

        CandidateProfile savedCandidate = candidateProfileRepository.save(candidateProfile);

        // Add skills if provided
        if (skillIds != null && !skillIds.isEmpty()) {
            List<Skill> skillsToAdd = skillRepository.findAllById(skillIds);

            // ĐẢM BẢO savedCandidate.getSkills() KHÔNG NULL
            if (savedCandidate.getSkills() == null) {
                savedCandidate.setSkills(new HashSet<>());
            }

            savedCandidate.getSkills().addAll(skillsToAdd);
            savedCandidate = candidateProfileRepository.save(savedCandidate);
        }

        return savedCandidate;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidateProfile> getCandidateProfileById(Long id) {
        log.info("Fetching candidate profile by id: {}", id);
        return candidateProfileRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidateProfile> getCandidateProfileByEmail(String email) {
        log.info("Fetching candidate profile by email: {}", email);
        return candidateProfileRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateProfile> getAllCandidateProfiles() {
        log.info("Fetching all candidate profiles");
        return candidateProfileRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidateProfile> getCandidateProfiles(Pageable pageable) {
        log.info("Fetching candidate profiles with pagination - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<CandidateProfile> page = candidateProfileRepository.findAll(pageable);

        log.info("Total candidates found: {}", page.getTotalElements());
        log.info("Current page candidates count: {}", page.getContent().size());

        // Debug: Hiển thị thông tin từng candidate
        for (CandidateProfile candidate : page.getContent()) {
            log.info("Candidate: {} (ID: {}), Skills count: {}",
                    candidate.getFullName(),
                    candidate.getId(),
                    candidate.getSkills() != null ? candidate.getSkills().size() : 0);
        }

        return page;
    }


    @Override
    public CandidateProfile updateCandidateProfile(Long id, CandidateProfile candidateProfile) {
        log.info("Updating candidate profile with id: {}", id);

        // Validate input data
        validateCandidateData(candidateProfile);

        return candidateProfileRepository.findById(id)
                .map(existingProfile -> {
                    existingProfile.setFullName(candidateProfile.getFullName());
                    existingProfile.setPhone(candidateProfile.getPhone());
                    existingProfile.setCareerGoal(candidateProfile.getCareerGoal());
                    existingProfile.setEducation(candidateProfile.getEducation());
                    existingProfile.setExperienceYears(candidateProfile.getExperienceYears());
                    existingProfile.setPreviousCompany(candidateProfile.getPreviousCompany());
                    existingProfile.setCertificates(candidateProfile.getCertificates());
                    return candidateProfileRepository.save(existingProfile);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ ứng viên với ID: " + id));
    }

    @Override
    public void deleteCandidateProfile(Long id) {
        log.info("Deleting candidate profile with id: {}", id);
        candidateProfileRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CandidateProfile> searchByKeyword(String keyword) {
        log.info("Searching candidate profiles by keyword: {}", keyword);
        return candidateProfileRepository.searchByKeyword(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateProfile> findBySkillNames(List<String> skillNames) {
        log.info("Searching candidate profiles by skill names: {}", skillNames);
        if (skillNames.isEmpty()) {
            return List.of();
        }
        return candidateProfileRepository.findBySkillName(skillNames.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateProfile> findByExperienceRange(Integer minExp, Integer maxExp) {
        log.info("Searching candidate profiles by experience range: {} - {} years", minExp, maxExp);
        return candidateProfileRepository.findByExperienceRange(minExp, maxExp);
    }

    @Override
    public CandidateProfile addSkillsToCandidate(Long candidateId, List<Long> skillIds) {
        log.info("Adding skills to candidate with id: {}", candidateId);

        // Validate skills
        validateSkills(skillIds);

        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ứng viên với ID: " + candidateId));

        List<Skill> skillsToAdd = skillRepository.findAllById(skillIds);

        // Initialize the skills set if null
        if (candidate.getSkills() == null) {
            candidate.setSkills(new HashSet<>());
        }

        candidate.getSkills().addAll(skillsToAdd);

        CandidateProfile saved = candidateProfileRepository.save(candidate);

        // Force initialization of skills to avoid LazyInitializationException
        saved.getSkills().size();

        return saved;
    }

    /**
     * Validate candidate data
     */
    private void validateCandidateData(CandidateProfile candidateProfile) {
        // Validate full name
        if (candidateProfile.getFullName() == null || candidateProfile.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống");
        }

        String fullName = candidateProfile.getFullName().trim();
        if (fullName.length() < 2 || fullName.length() > 255) {
            throw new RuntimeException("Họ tên phải có độ dài từ 2 đến 255 ký tự");
        }

        if (!NAME_PATTERN.matcher(fullName).matches()) {
            throw new RuntimeException("Họ tên chỉ được chứa chữ cái và khoảng trắng");
        }

        // Validate email
        if (candidateProfile.getEmail() == null || candidateProfile.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống");
        }

        String email = candidateProfile.getEmail().trim();
        if (email.length() > 255) {
            throw new RuntimeException("Email không được vượt quá 255 ký tự");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("Email không hợp lệ");
        }

        // Validate phone (required field)
        if (candidateProfile.getPhone() == null || candidateProfile.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }

        String phone = candidateProfile.getPhone().trim();
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new RuntimeException("Số điện thoại phải là 10 chữ số");
        }

        // Validate education (required field)
        if (candidateProfile.getEducation() == null || candidateProfile.getEducation().trim().isEmpty()) {
            throw new RuntimeException("Trình độ học vấn không được để trống");
        }

        String education = candidateProfile.getEducation().trim();
        if (!VALID_EDUCATION_LEVELS.contains(education)) {
            throw new RuntimeException("Trình độ học vấn không hợp lệ. Chỉ chấp nhận: " + VALID_EDUCATION_LEVELS);
        }

        // Validate experience years
        if (candidateProfile.getExperienceYears() < 0) {
            throw new RuntimeException("Số năm kinh nghiệm không được âm");
        }

        if (candidateProfile.getExperienceYears() > 50) {
            throw new RuntimeException("Số năm kinh nghiệm không được vượt quá 50 năm");
        }
    }

    /**
     * Validate skills
     */
    private void validateSkills(List<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) {
            throw new RuntimeException("Phải chọn ít nhất một kỹ năng");
        }

        // Check if all skill IDs exist
        List<Skill> existingSkills = skillRepository.findAllById(skillIds);
        if (existingSkills.size() != skillIds.size()) {
            throw new RuntimeException("Một số kỹ năng không tồn tại trong hệ thống");
        }

        // Check for duplicate skill IDs
        long distinctCount = skillIds.stream().distinct().count();
        if (distinctCount != skillIds.size()) {
            throw new RuntimeException("Có kỹ năng bị trùng lặp");
        }
    }

    /**
     * Validate candidate data for specific fields (for partial updates)
     */
    public void validateCandidateField(String fieldName, String value) {
        switch (fieldName) {
            case "fullName":
                validateFullName(value);
                break;
            case "email":
                validateEmail(value);
                break;
            case "phone":
                validatePhone(value);
                break;
            case "education":
                validateEducation(value);
                break;
            default:
                throw new RuntimeException("Trường dữ liệu không được hỗ trợ: " + fieldName);
        }
    }

    private void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống");
        }

        String name = fullName.trim();
        if (name.length() < 2 || name.length() > 255) {
            throw new RuntimeException("Họ tên phải có độ dài từ 2 đến 255 ký tự");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new RuntimeException("Họ tên chỉ được chứa chữ cái và khoảng trắng");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống");
        }

        String emailValue = email.trim();
        if (emailValue.length() > 255) {
            throw new RuntimeException("Email không được vượt quá 255 ký tự");
        }

        if (!EMAIL_PATTERN.matcher(emailValue).matches()) {
            throw new RuntimeException("Email không hợp lệ");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }

        String phoneValue = phone.trim();
        if (!PHONE_PATTERN.matcher(phoneValue).matches()) {
            throw new RuntimeException("Số điện thoại phải là 10 chữ số");
        }
    }

    private void validateEducation(String education) {
        if (education == null || education.trim().isEmpty()) {
            throw new RuntimeException("Trình độ học vấn không được để trống");
        }

        String educationValue = education.trim();
        if (!VALID_EDUCATION_LEVELS.contains(educationValue)) {
            throw new RuntimeException("Trình độ học vấn không hợp lệ. Chỉ chấp nhận: " + VALID_EDUCATION_LEVELS);
        }
    }



    @Override
    public CandidateProfile removeSkillsFromCandidate(Long candidateId, List<Long> skillIds) {
        log.info("Removing skills from candidate with id: {}", candidateId);

        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ứng viên với ID: " + candidateId));

        candidate.getSkills().removeIf(skill -> skillIds.contains(skill.getId()));

        return candidateProfileRepository.save(candidate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return candidateProfileRepository.existsByEmail(email);
    }
}