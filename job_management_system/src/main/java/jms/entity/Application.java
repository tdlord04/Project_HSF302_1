package jms.entity;

import jakarta.persistence.*;
import jms.entity.enums.ApplicationStatus;
import lombok.*;

/**
 * Đơn ứng tuyển của ứng viên
 */

@Entity
@Table(name = "application", indexes = {
        @Index(name = "idx_application_status", columnList = "status"),
        @Index(name = "idx_application_candidate", columnList = "candidate_id"),
        @Index(name = "idx_application_job", columnList = "job_posting_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate; // Ứng viên nộp đơn

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting; // Tin tuyển dụng ứng tuyển

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private ApplicationStatus status; // APPLIED, INTERVIEWING, REJECTED, OFFERED, HIRED

    @Column(name = "note", columnDefinition = "NVARCHAR(MAX)")
    private String note; // Ghi chú của HR

    @Column(name = "source", columnDefinition = "NVARCHAR(255)")
    private String source; // Nguồn ứng tuyển (LinkedIn, Website, Email, v.v.)
}
