package jms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Thông tin công ty
 */

@Entity
@Table(name = "company", indexes = {
        @Index(name = "idx_company_name", columnList = "name")
})
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company extends BaseEntity {

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name; // Tên công ty

    @Column(name = "address", columnDefinition = "NVARCHAR(500)")
    private String address; // Địa chỉ

    @Column(name = "email", columnDefinition = "NVARCHAR(255)")
    private String email; // Email công ty

    @Column(name = "phone", columnDefinition = "NVARCHAR(50)")
    private String phone; // Số điện thoại

    @Column(name = "website", columnDefinition = "NVARCHAR(255)")
    private String website; // Trang web
}

