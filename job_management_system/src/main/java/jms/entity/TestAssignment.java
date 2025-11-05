package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Giao bài test cho ứng viên
 */

@Entity
@Table(name = "test_assignment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestAssignment extends BaseEntity {

    /** Tiêu đề bài test */
    @Column(name = "test_title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String testTitle;

    /** Mô tả bài test */
    @Column(name = "description", columnDefinition = "NVARCHAR(1000)")
    private String description;

    /** Danh sách câu hỏi trong bài test */
    @ManyToMany
    @JoinTable(name = "assessment_question",
            joinColumns = @JoinColumn(name = "assessment_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions;

    @Column(name = "deadline")
    private Instant deadline; // Hạn nộp bài test
}

