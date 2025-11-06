package jms.controller.admin;

import jms.dto.UserAccountDTO;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserAccountService userAccountService;

    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UserAccountDTO> userPage = userAccountService.findAllFiltered(role, status, keyword, pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("role", role);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

        // Enum để hiển thị select option
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", UserStatus.values());

        return "admin/user-list";
    }

    @GetMapping("/api")
    public ResponseEntity<Page<UserAccountDTO>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAccountDTO> userPage = userAccountService.findAllFiltered(role, status, keyword, pageable);
        return ResponseEntity.ok(userPage);
    }
}
