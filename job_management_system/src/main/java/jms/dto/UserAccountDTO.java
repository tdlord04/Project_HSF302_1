package jms.dto;

import jakarta.validation.constraints.*;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * DTO dùng cho Admin để CRUD tài khoản người dùng
 * (bao gồm cả thông tin đăng nhập và thông tin cá nhân).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountDTO {

    // ==== ACCOUNT INFO ====
    private Long accountId;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6–100 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,100}$",
            message = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt"
    )
    private String password; // có thể null khi update

    @NotNull(message = "Vai trò không được để trống")
    private Role role;

    @NotNull(message = "Trạng thái không được để trống")
    private UserStatus status;

    // ==== USER INFO ====
    private Long userId;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255, message = "Họ tên tối đa 255 ký tự")
    private String fullName;

    @Pattern(
            regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$",
            message = "Số điện thoại không hợp lệ (VD: 090xxxxxxx hoặc +8490xxxxxxx)"
    )
    private String phone;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @NotNull(message = "Công ty không được để trống")
    private Long companyId;

    private String companyName; // chỉ dùng hiển thị

    // ==== AUDIT INFO ====
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updatedAt;
}
