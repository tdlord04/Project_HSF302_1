package jms.controller.auth;

import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import jms.dto.auth.LoginRequest;
import jms.entity.Account;
import jms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult result,
            HttpSession session,
            HttpServletResponse response,
            Model model
    ) {
        if (result.hasErrors()) return "auth/login";

        Account account = authService.authenticate(loginRequest);
        if (account == null) {
            model.addAttribute("loginError", "Sai email hoặc mật khẩu!");
            return "auth/login";
        }

        authService.login(session, response, account, loginRequest.isRememberMe());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        authService.logout(session, response);
        return "redirect:/auth/login?logout";
    }
}
