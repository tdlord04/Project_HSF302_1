package jms.security;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class PasswordEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Mã hóa mật khẩu
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Kiểm tra mật khẩu (không có "giải mã" thực sự vì bcrypt là one-way)
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("Aa@123"));
    }
}