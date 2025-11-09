package jms.controller.admin;

import jakarta.validation.Valid;
import jms.dto.UserAccountDTO;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.repository.CompanyRepository;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/admin/users","/", "/home"})
@RequiredArgsConstructor
public class AdminUserController {

    private final UserAccountService userAccountService;
    private final CompanyRepository companyRepository;

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

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("userAccountDTO", new UserAccountDTO());
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("companies", companyRepository.findAll()); // nếu có danh sách công ty
        return "admin/user-form";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("userAccountDTO") UserAccountDTO dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("statuses", UserStatus.values());
            model.addAttribute("companies", companyRepository.findAll());
            return "admin/user-form";
        }

        userAccountService.createUser(dto);
        return "redirect:/admin/users?success";
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
