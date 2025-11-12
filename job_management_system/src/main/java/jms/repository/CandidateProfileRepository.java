// CandidateProfileRepository.java
package jms.repository;

import jms.entity.CandidateProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByEmail(String email);

    List<CandidateProfile> findByFullNameContainingIgnoreCase(String fullName);

    Page<CandidateProfile> findByExperienceYearsGreaterThanEqual(Integer years, Pageable pageable);

    @Query("SELECT cp FROM CandidateProfile cp JOIN cp.skills s WHERE s.name = :skillName")
    List<CandidateProfile> findBySkillName(@Param("skillName") String skillName);

    @Query("SELECT cp FROM CandidateProfile cp WHERE LOWER(cp.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(cp.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CandidateProfile> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT cp FROM CandidateProfile cp WHERE cp.experienceYears BETWEEN :minExp AND :maxExp")
    List<CandidateProfile> findByExperienceRange(@Param("minExp") Integer minExp, @Param("maxExp") Integer maxExp);

    boolean existsByEmail(String email);
}