package jms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Ghi chú đánh giá ứng viên
 */

@Entity
@Table(name = "evaluation_note", indexes = {
        @Index(name = "idx_eval_interview", columnList = "interview_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluationNote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview; // Gắn với buổi phỏng vấn

    @ManyToOne
    @JoinColumn(name = "evaluator_id")
    private User evaluator; // Người đánh giá

    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content; // Nội dung nhận xét

    @Column(name = "rating")
    private Integer rating; // Điểm đánh giá (1–5)
}
