package jms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity cơ sở cho các bảng khác
 * Cung cấp id tự tăng và thời gian tạo/cập nhật.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính tự tăng

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt; // Thời điểm tạo bản ghi

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt; // Thời điểm cập nhật gần nhất

    @Column(name = "delete_at")
    private Instant deletedAt; // Thời điểm xóa mềm
}