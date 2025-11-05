package jms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Giai đoạn trong quy trình tuyển dụng
 */

@Entity
@Table(name = "recruitment_stage", indexes = {
        @Index(name = "idx_stage_name", columnList = "stage_name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecruitmentStage extends BaseEntity {

    @Column(name = "stage_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String stageName; // Tên giai đoạn (Ví dụ: Sàng lọc, Phỏng vấn, Đề nghị offer)

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả chi tiết giai đoạn
}
