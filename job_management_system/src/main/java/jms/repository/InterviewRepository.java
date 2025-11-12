package jms.repository;

import jms.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // Basic queries
    List<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByInterviewerId(Long interviewerId);
    List<Interview> findByInterviewDateAfter(LocalDateTime date);

    // NEW: Pagination for interviewer filter
    Page<Interview> findByInterviewerId(Long interviewerId, Pageable pageable);

    // Count queries
    Long countByResult(String result);

    // Custom count queries
    @Query("SELECT COUNT(i) FROM Interview i WHERE i.result = 'PASSED'")
    Long countPassedInterviews();

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.result = 'PENDING'")
    Long countPendingInterviews();

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.result = 'FAILED'")
    Long countFailedInterviews();

    // Custom query for upcoming interviews by interviewer
    @Query("SELECT i FROM Interview i WHERE i.interviewer.id = :interviewerId AND i.interviewDate > :now ORDER BY i.interviewDate ASC")
    List<Interview> findUpcomingInterviewsByInterviewer(@Param("interviewerId") Long interviewerId, @Param("now") LocalDateTime now);



    // EntityGraph for eager loading
    @EntityGraph(attributePaths = {"application", "application.candidate", "application.jobPosting", "interviewer"})
    Page<Interview> findAll(Pageable pageable);

    // Thêm các method này vào InterviewRepository
    @Query("SELECT COUNT(e) > 0 FROM EvaluationNote e WHERE e.interview.id = :interviewId")
    boolean existsEvaluationNotesByInterviewId(@Param("interviewId") Long interviewId);

    @Modifying
    @Query("DELETE FROM EvaluationNote e WHERE e.interview.id = :interviewId")
    void deleteEvaluationNotesByInterviewId(@Param("interviewId") Long interviewId);

    @Query("""
    SELECT i FROM Interview i 
    LEFT JOIN i.application a 
    LEFT JOIN a.candidate c 
    LEFT JOIN a.jobPosting j 
    LEFT JOIN i.interviewer iv 
    WHERE (:keyword IS NULL OR :keyword = '' OR 
           LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(iv.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(i.notes) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND (:result IS NULL OR :result = '' OR i.result = :result)
    AND (:interviewerId IS NULL OR iv.id = :interviewerId)
    AND (:fromDate IS NULL OR CAST(i.interviewDate AS date) >= :fromDate)
    AND (:toDate IS NULL OR CAST(i.interviewDate AS date) <= :toDate)
""")
    @EntityGraph(attributePaths = {"application", "application.candidate", "application.jobPosting", "interviewer"})
    Page<Interview> searchInterviews(@Param("keyword") String keyword,
                                     @Param("result") String result,
                                     @Param("interviewerId") Long interviewerId,
                                     @Param("fromDate") LocalDate fromDate,
                                     @Param("toDate") LocalDate toDate,
                                     Pageable pageable);
}

