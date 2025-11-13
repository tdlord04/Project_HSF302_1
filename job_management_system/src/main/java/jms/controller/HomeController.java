package jms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/dashboard", "/"})
public class HomeController {

    @GetMapping
    public String home(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "dashboard";
    }
}
