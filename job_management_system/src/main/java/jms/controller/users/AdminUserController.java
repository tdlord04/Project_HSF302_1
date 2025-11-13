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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = {"/users"})
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','HR')")
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

        return "users/user-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        UserAccountDTO dto = new UserAccountDTO();
        model.addAttribute("userAccountDTO", dto);
        model.addAttribute("editMode", false);

        addCommonAttributes(model);
        return "users/user-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("userAccountDTO") UserAccountDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("editMode", false);
            return "users/user-form";
        }
        userAccountService.createUser(dto);
        return "redirect:/users?success";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        UserAccountDTO dto = userAccountService.findUserById(id);
        model.addAttribute("userAccountDTO", dto);
        model.addAttribute("editMode", true);
        addCommonAttributes(model);
        return "users/user-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("userAccountDTO") UserAccountDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("editMode", true);
            return "users/user-form";
        }
        userAccountService.updateUser(id, dto);
        return "redirect:/users?success";
    }

    private void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String roleName = authentication.getAuthorities().iterator().next().getAuthority();
        roleName = roleName.replace("ROLE_", "");
        Role currentRole = Role.valueOf(roleName);
        List<Role> roles = currentRole.getAssignableRoles();

        List<UserStatus> statuses = Arrays.asList(UserStatus.values());
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);
        model.addAttribute("companies", companyRepository.findAll());
    }

    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')") // Chỉ users mới xóa được
    public String deleteUser(@PathVariable("id") Long userId) {
        userAccountService.deleteUser(userId);
        return "redirect:/users?deleted";
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
