package jms.repository;

import jms.entity.User;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u
        JOIN u.account a
        JOIN u.company c
        WHERE u.deletedAt IS NULL
          AND (:role IS NULL OR a.role = :role)
          AND (:status IS NULL OR a.status = :status)
          AND (:keyword IS NULL OR
               LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
               LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<User> findAllFiltered(
            @Param("role") Role role,
            @Param("status") UserStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
