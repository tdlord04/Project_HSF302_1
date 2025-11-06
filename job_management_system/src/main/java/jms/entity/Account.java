package jms.entity;

import jakarta.persistence.*;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import lombok.*;

/**
 * Tài khoản đăng nhập của người dùng (HR, nhân viên, ứng viên, quản lý,...)
 */
@Entity
@Table(name = "account", indexes = {
        @Index(name = "idx_account_username", columnList = "username"),
        @Index(name = "idx_account_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Account extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, columnDefinition = "NVARCHAR(100)")
    private String username; // Tên đăng nhập

    @Column(name = "email", unique = true, nullable = false, columnDefinition = "NVARCHAR(255)")
    private String email; // Email đăng nhập

    @Column(name = "password_hash", nullable = false)
    private String passwordHash; // Mật khẩu mã hóa

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private Role role; // Quyền (ADMIN, HR, MANAGER, INTERVIEWER)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private UserStatus status = UserStatus.ACTIVE; // ACTIVE / INACTIVE / LOCKED
}
