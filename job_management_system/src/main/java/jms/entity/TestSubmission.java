package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Bài làm của ứng viên
 */

@Entity
@Table(name = "test_submission", indexes = {
        @Index(name = "idx_submission_assignment", columnList = "test_assignment_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestSubmission extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "test_assignment_id", nullable = false)
    private TestAssignment testAssignment; // Bài test được giao

    @Column(name = "submitted_at")
    private Instant submittedAt; // Thời gian nộp

    @Column(name = "score")
    private Double score; // Điểm số

    @Column(name = "answers", columnDefinition = "NVARCHAR(MAX)")
    private String answers; // Câu trả lời (JSON string)
}
