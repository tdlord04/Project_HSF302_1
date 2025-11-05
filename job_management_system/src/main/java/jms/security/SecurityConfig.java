package jms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return username -> null; // Không tạo user mặc định nữa
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())         // Tắt CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()        // Cho phép tất cả request, không cần login
                )
                .formLogin(login -> login.disable())  // Tắt form login
                .httpBasic(basic -> basic.disable()); // Tắt basic auth

        return http.build();
    }
}
