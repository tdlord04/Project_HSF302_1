package jms.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Thông tin của nhân viên
*/

@Entity
@Table(name = "[user]", indexes = {
        @Index(name = "idx_user_full_name", columnList = "full_name"),
        @Index(name = "idx_user_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String fullName; // Họ tên nhân viên

    @Column(name = "position", columnDefinition = "NVARCHAR(255)")
    private String position; // Chức vụ

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; // Công ty làm việc

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account; // Tài khoản đăng nhập
}
