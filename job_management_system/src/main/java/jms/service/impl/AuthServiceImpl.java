package jms.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jms.dto.auth.LoginRequest;
import jms.entity.Account;
import jms.repository.AccountRepository;
import jms.security.CookieUtil;
import jms.security.PasswordEncoder;
import jms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;

    @Override
    public Account authenticate(LoginRequest loginRequest) {
        return accountRepository.findAll().stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(loginRequest.getEmail()))
                .filter(a -> PasswordEncoder.matches(loginRequest.getPassword(), a.getPasswordHash()))
                .findFirst()
                .orElse(null);
    }

    public void login(HttpSession session, HttpServletResponse response, Account account, boolean rememberMe) {
        session.setAttribute("account", account);

        if (rememberMe) {
            CookieUtil.addCookie(response, "rememberEmail", account.getEmail(), 7 * 24 * 60 * 60);
        }
    }

    public void logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        CookieUtil.clearCookie(response, "rememberEmail");
    }
}
