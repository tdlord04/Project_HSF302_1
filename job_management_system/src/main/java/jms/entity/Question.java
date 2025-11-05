package jms.entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Câu hỏi trong bộ bài test hoặc phỏng vấn.
 */
@Entity
@Table(name = "question", indexes = {
        @Index(name = "idx_question_text", columnList = "question_text")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseEntity {

    /** Nội dung câu hỏi */
    @Column(name = "question_text", columnDefinition = "NVARCHAR(1000)", nullable = false)
    private String questionText;

    /** Đáp án đúng (nếu là trắc nghiệm) */
    @Column(name = "correct_answer", columnDefinition = "NVARCHAR(255)")
    private String correctAnswer;

    /** Loại câu hỏi (TEXT, MULTIPLE_CHOICE, CODING, ESSAY...)*/
    @Column(name = "question_type", columnDefinition = "NVARCHAR(50)")
    private String questionType;
}