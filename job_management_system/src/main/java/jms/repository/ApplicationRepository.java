// jms/repository/ApplicationRepository.java
package jms.repository;

import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Lấy tất cả application trong khoảng createdAt, có thể lọc theo company ở
    // Service
    List<Application> findByCreatedAtBetween(Instant start, Instant end);

    // Lấy tất cả OFFERED trong khoảng updatedAt (để tính avg time to offer);
    // fallback sẽ xử lý ở Service nếu thiếu updatedAt
    @Query("SELECT a FROM Application a " +
            "WHERE a.status = :status AND a.updatedAt BETWEEN :start AND :end")
    List<Application> findOfferedBetween(ApplicationStatus status, Instant start, Instant end);

    // Trong ApplicationRepository
    @Query("SELECT a FROM Application a JOIN FETCH a.candidate JOIN FETCH a.jobPosting")
    List<Application> findAllWithCandidateAndJobPosting();
}
