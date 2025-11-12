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

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final SkillRepository skillRepository;

    @Override
    public CandidateProfile createCandidateProfile(CandidateProfile candidateProfile) {
        log.info("Creating candidate profile for email: {}", candidateProfile.getEmail());

        if (candidateProfileRepository.existsByEmail(candidateProfile.getEmail())) {
            throw new RuntimeException("Candidate with email " + candidateProfile.getEmail() + " already exists");
        }

        return candidateProfileRepository.save(candidateProfile);
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
        log.info("Fetching candidate profiles with pagination");
        return candidateProfileRepository.findAll(pageable);
    }

    @Override
    public CandidateProfile updateCandidateProfile(Long id, CandidateProfile candidateProfile) {
        log.info("Updating candidate profile with id: {}", id);

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
                .orElseThrow(() -> new RuntimeException("Candidate profile not found with id: " + id));
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

        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        List<Skill> skillsToAdd = skillRepository.findAllById(skillIds);
        candidate.getSkills().addAll(skillsToAdd);

        return candidateProfileRepository.save(candidate);
    }

    @Override
    public CandidateProfile removeSkillsFromCandidate(Long candidateId, List<Long> skillIds) {
        log.info("Removing skills from candidate with id: {}", candidateId);

        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        candidate.getSkills().removeIf(skill -> skillIds.contains(skill.getId()));

        return candidateProfileRepository.save(candidate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return candidateProfileRepository.existsByEmail(email);
    }
}