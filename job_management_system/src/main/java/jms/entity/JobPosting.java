package jms.entity;

import jakarta.persistence.*;
import jms.entity.enums.JobStatus;
import lombok.*;

/**
 * Thông tin đăng tuyển
 */

@Entity
@Table(name = "job_posting", indexes = {
        @Index(name = "idx_job_title", columnList = "job_title"),
        @Index(name = "idx_job_status", columnList = "status"),
        @Index(name = "idx_job_company", columnList = "company_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPosting extends BaseEntity {

    @Column(name = "job_title", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String jobTitle; // Tên vị trí tuyển dụng

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả công việc

    @Column(name = "requirements", columnDefinition = "NVARCHAR(MAX)")
    private String requirements; // Yêu cầu công việc

    @Column(name = "location", columnDefinition = "NVARCHAR(255)")
    private String location; // Địa điểm làm việc

    @Column(name = "salary_range", columnDefinition = "NVARCHAR(255)")
    private String salaryRange; // Mức lương dự kiến

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private JobStatus status; // OPEN / CLOSED / PAUSED

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; // Công ty đăng tuyển
}

