package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Cuộc phỏng vấn
 */
@Entity
@Table(name = "interview", indexes = {
        @Index(name = "idx_interview_date", columnList = "interview_date"),
        @Index(name = "idx_interview_application", columnList = "application_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Interview extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application; // Hồ sơ ứng tuyển

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer; // Người phỏng vấn

    @Column(name = "interview_date")
    private Instant interviewDate; // Ngày và giờ phỏng vấn

    @Column(name = "location", columnDefinition = "NVARCHAR(255)")
    private String location; // Địa điểm phỏng vấn (có thể là online link)

    @Column(name = "result", columnDefinition = "NVARCHAR(255)")
    private String result; // Kết quả phỏng vấn (Pass, Fail, Pending)

    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes; // Ghi chú thêm
}
