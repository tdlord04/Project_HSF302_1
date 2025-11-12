package jms.security;

import jms.service.impl.UserAccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Cho phép @PreAuthorize
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // method injection UserAccountServiceImpl -> không gây circular dependency
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserAccountServiceImpl userAccountServiceImpl) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userAccountServiceImpl); // sử dụng service trực tiếp
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserAccountServiceImpl userAccountServiceImpl) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
//                        .requestMatchers("/hr/**").hasAnyRole("HR", "ADMIN")
//                        .requestMatchers("/interview/**").hasAnyRole("INTERVIEWER", "ADMIN")
                        .anyRequest().permitAll()
                )
//                .formLogin(form -> form
//                        .loginPage("/auth/login")             // GET login page
//                        .loginProcessingUrl("/auth/login")    // POST action
//                        .successHandler((req, res, auth) -> {
//                            String contextPath = req.getContextPath();
//                            res.sendRedirect(contextPath + "/");
//                        })
//                        .failureUrl("/auth/login?error=true")
//                        .permitAll()
//                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("rememberKey123")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .userDetailsService(userAccountServiceImpl)
                );

        return http.build();
    }

}
