package jms.controller.users;

import jakarta.validation.Valid;
import jms.dto.UserAccountDTO;
import jms.entity.Account;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.repository.AccountRepository;
import jms.repository.CompanyRepository;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = {"/manager/users"})
@RequiredArgsConstructor
public class AdminUserController {

    private final UserAccountService userAccountService;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;

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

        return "users/user-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // chính là email đăng nhập
        Account current = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("userAccountDTO", new UserAccountDTO());
        model.addAttribute("roles", current.getRole().getAssignableRoles());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("companies", companyRepository.findAll()); // nếu có danh sách công ty
        return "users/user-form";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("userAccountDTO") UserAccountDTO dto,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername(); // chính là email đăng nhập
        Account current = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (bindingResult.hasErrors()) {
            List<Role> assignable = current.getRole().getAssignableRoles();
            model.addAttribute("roles", assignable);
            model.addAttribute("statuses", UserStatus.values());
            model.addAttribute("companies", companyRepository.findAll());
            return "users/user-form";
        }

        userAccountService.createUser(dto);
        return "redirect:/users/users?success";
    }

    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')") // Chỉ users mới xóa được
    public String deleteUser(@PathVariable("id") Long userId) {
        userAccountService.deleteUser(userId);
        return "redirect:/manager/users?deleted";
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
