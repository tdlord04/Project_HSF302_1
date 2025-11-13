// CandidateProfileService.java
package jms.service;

import jms.entity.CandidateProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CandidateProfileService {
    CandidateProfile createCandidateProfile(CandidateProfile candidateProfile, List<Long> skillIds);
    Optional<CandidateProfile> getCandidateProfileById(Long id);
    Optional<CandidateProfile> getCandidateProfileByEmail(String email);
    List<CandidateProfile> getAllCandidateProfiles();
    Page<CandidateProfile> getCandidateProfiles(Pageable pageable);
    CandidateProfile updateCandidateProfile(Long id, CandidateProfile candidateProfile);
    void deleteCandidateProfile(Long id);
    List<CandidateProfile> searchByKeyword(String keyword);
    List<CandidateProfile> findBySkillNames(List<String> skillNames);
    List<CandidateProfile> findByExperienceRange(Integer minExp, Integer maxExp);
    CandidateProfile addSkillsToCandidate(Long candidateId, List<Long> skillIds);
    CandidateProfile removeSkillsFromCandidate(Long candidateId, List<Long> skillIds);
    boolean existsByEmail(String email);
}