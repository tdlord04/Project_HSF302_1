package jms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Báo cáo phân tích hiệu quả tuyển dụng (KPI, tỷ lệ, nguồn ứng viên,...).
 */
@Entity
@Table(name = "report", indexes = {
        @Index(name = "idx_report_type", columnList = "report_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    /** Loại báo cáo (VD: “Recruitment Funnel”, “KPI by Department”) */
    @Column(name = "report_type", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String reportType;

    /** Dữ liệu dạng JSON */
    @Column(name = "data", columnDefinition = "NVARCHAR(MAX)")
    private String data;

    /** Khoảng thời gian báo cáo */
    @Column(name = "time_range", columnDefinition = "NVARCHAR(100)")
    private String timeRange;
}