package jms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

/**
 * Form tuyển dụng tùy chỉnh, cho phép HR thêm trường riêng theo từng tin đăng.
 * Dùng để mở rộng form ứng tuyển (VD: thêm câu hỏi tự luận, chọn kỹ năng,...)
 */
@Entity
@Table(name = "custom_form", indexes = {
        @Index(name = "idx_custom_form_job_posting_id", columnList = "job_posting_id"),
        @Index(name = "idx_custom_form_name", columnList = "form_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomForm extends BaseEntity {

    /**
     * Tên form (VD: “Form ứng tuyển Lập trình viên Java”)
     */
    @Column(name = "form_name", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String formName;

    /**
     * Mô tả ngắn về form
     */
    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String description;

    /**
     * Cấu trúc form lưu dạng JSON (VD: [{“label”: “Kỹ năng”, “type”: “checkbox”, “options”: […]}, ...])
     */
    @Lob
    @Column(name = "form_structure_json", columnDefinition = "NVARCHAR(MAX)")
    private String formStructureJson;

    /**
     * Tin tuyển dụng mà form này gắn liền
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    /**
     * Trạng thái form (true = đang sử dụng, false = ngừng)
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}