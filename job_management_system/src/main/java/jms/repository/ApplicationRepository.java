// ApplicationRepository.java
package jms.repository;

import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByJobPostingId(Long jobPostingId);

    List<Application> findByStatus(ApplicationStatus status);

    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidateId AND a.jobPosting.id = :jobPostingId")
    Optional<Application> findByCandidateAndJobPosting(@Param("candidateId") Long candidateId,
                                                       @Param("jobPostingId") Long jobPostingId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.candidate.id = :candidateId")
    Long countApplicationsByCandidateId(@Param("candidateId") Long candidateId);

    boolean existsByCandidateIdAndJobPostingId(Long candidateId, Long jobPostingId);

    List<Application> findByCreatedAtBetween(Instant startAt, Instant endAt);
}