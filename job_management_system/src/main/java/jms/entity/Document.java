package jms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Đại diện cho tài liệu của ứng viên (CV, thư xin việc, chứng chỉ scan...).
 */
@Entity
@Table(name = "document", indexes = {
        @Index(name = "idx_document_candidate_id", columnList = "candidate_id"),
        @Index(name = "idx_document_name", columnList = "document_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends BaseEntity {

    /** Ứng viên sở hữu tài liệu này */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    /** Tên tài liệu (VD: “CV tiếng Anh”, “Chứng chỉ TOEIC”) */
    @Column(name = "document_name", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String documentName;

    /** Đường dẫn lưu file (server hoặc cloud) */
    @Column(name = "file_path", columnDefinition = "NVARCHAR(500)", nullable = false)
    private String filePath;

    /** Loại file (CV, cover letter, certificate...) */
    @Column(name = "file_type", columnDefinition = "NVARCHAR(100)")
    private String fileType;

    /** Ngày upload */
    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private Instant uploadedAt;

    /** Ngày chỉnh sửa */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}