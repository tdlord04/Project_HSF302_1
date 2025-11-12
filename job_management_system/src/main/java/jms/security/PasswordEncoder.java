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
//        String[] users = {
//                "users", "hr01", "hr02", "manager01", "manager02",
//                "interviewer01", "interviewer02",
//                "candidate01", "candidate02", "candidate03", "candidate04", "candidate05"
//        };
//
//        for (String user : users) {
//            String hash = PasswordEncoder.encrypt("123456");
//            System.out.printf("UPDATE account SET password_hash = '%s' WHERE username = '%s';%n", hash, user);
//        }

        String raw = "123456";
        String hash = "$2a$10$Kpxh.5.DFdBTlrPmGVqYCe5DiEHzqyWdourJASxPK8GCwm0KxyUyG";
        boolean ok = new BCryptPasswordEncoder().matches(raw, hash);
        System.out.println(ok); // phải true
    }
}