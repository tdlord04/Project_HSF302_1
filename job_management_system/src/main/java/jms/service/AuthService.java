package jms.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jms.dto.auth.LoginRequest;
import jms.entity.Account;

public interface AuthService {
    Account authenticate(LoginRequest loginRequest);

    void login(HttpSession session, HttpServletResponse response, Account account, boolean rememberMe);

    void logout(HttpSession session, HttpServletResponse response);
}
