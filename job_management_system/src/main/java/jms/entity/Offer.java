package jms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Quản lý offer và hợp đồng cho ứng viên được tuyển.
 */
@Entity
@Table(name = "offer", indexes = {
        @Index(name = "idx_offer_application_id", columnList = "application_id"),
        @Index(name = "idx_offer_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer extends BaseEntity {

    /** Ứng viên được đề nghị offer */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    /** Vị trí tuyển dụng */
    @Column(name = "position_title", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String positionTitle;

    /** Mức lương đề xuất */
    @Column(name = "salary_offer")
    private Double salaryOffer;

    /** Ngày bắt đầu dự kiến */
    @Column(name = "start_date")
    private LocalDate startDate;

    /** Trạng thái offer (PENDING, ACCEPTED, REJECTED) */
    @Column(name = "status", columnDefinition = "NVARCHAR(50)")
    private String status;
}

