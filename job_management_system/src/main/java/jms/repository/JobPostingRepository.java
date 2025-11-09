package jms.repository;

import jms.entity.JobPosting;
import jms.entity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByStatus(JobStatus status);

    List<JobPosting> findByJobTitleContainingIgnoreCase(String keyword);

    @Query("""
                SELECT j FROM JobPosting j
                LEFT JOIN j.company c
                WHERE
                    (:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%')))
                AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
                AND (:salaryRange IS NULL OR LOWER(j.salaryRange) LIKE LOWER(CONCAT('%', :salaryRange, '%')))
                AND (:requirements IS NULL OR LOWER(j.requirements) LIKE LOWER(CONCAT('%', :requirements, '%')))
                AND (:status IS NULL OR j.status = :status)
                AND (:companyName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :companyName, '%')))
            """)
    Page<JobPosting> advancedFilterPaged(
            @Param("title") String title,
            @Param("location") String location,
            @Param("salaryRange") String salaryRange,
            @Param("status") JobStatus status,
            @Param("requirements") String requirements,
            @Param("companyName") String companyName,
            Pageable pageable
    );

}
