package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Nhật ký hoạt động
 */

@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_audit_user", columnList = "user_id"),
        @Index(name = "idx_audit_action", columnList = "action")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Ai thực hiện hành động

    @Column(name = "action", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String action; // Loại hành động (CREATE_JOB, UPDATE_CANDIDATE,...)

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả chi tiết hành động

    @Column(name = "timestamp")
    private Instant timestamp; // Thời điểm xảy ra
}
