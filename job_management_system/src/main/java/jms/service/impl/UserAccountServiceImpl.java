package jms.service.impl;

import jms.dto.UserAccountDTO;
import jms.entity.Account;
import jms.entity.User;
import jms.entity.Company;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.exception.DuplicateFieldException;
import jms.exception.ResourceNotFoundException;
import jms.mapper.UserAccountMapper;
import jms.repository.AccountRepository;
import jms.repository.CompanyRepository;
import jms.repository.UserRepository;
import jms.service.AuditLogService;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserAccountMapper mapper;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Override
    public Page<UserAccountDTO> findAllFiltered(String roleStr, String statusStr, String keyword, Pageable pageable) {
        Role role = (roleStr == null || roleStr.isEmpty() || "null".equalsIgnoreCase(roleStr))
                ? null
                : Role.valueOf(roleStr.toUpperCase());

        UserStatus status = (statusStr == null || statusStr.isEmpty() || "null".equalsIgnoreCase(statusStr))
                ? null
                : UserStatus.valueOf(statusStr.toUpperCase());

        return userRepository.findAllFiltered(role, status, keyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public void createUser(UserAccountDTO dto) {

        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFieldException("Email đã tồn tại: " + dto.getEmail());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công ty có ID " + dto.getCompanyId()));

        Account account = mapper.toAccount(dto);
        account.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        accountRepository.save(account);

        User user = mapper.toUser(dto);
        user.setAccount(account);
        user.setCompany(company);
        userRepository.save(user);

        // Ghi Audit log
        auditLogService.log(
                user.getId(),
                "TẠO NGƯỜI DÙNG",
                "Tạo tài khoản cho: " + user.getFullName()
        );

        mapper.toDTO(user);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = user.getAccount();
        account.setDeletedAt(Instant.now());
        user.setDeletedAt(Instant.now());

        userRepository.save(user);

        // Ghi Audit log
        auditLogService.log(
                user.getId(),
                "XÓA NGƯỜI DÙNG",
                "Xóa mềm tài khoản: " + user.getFullName()
        );
    }

    @Override
    public UserAccountDTO findUserById(Long id) {
        return userRepository.findById(id).map(mapper::toDTO).orElseThrow(() -> new RuntimeException("User is not found"));
    }

    @Override
    public void updateUser(Long id, UserAccountDTO dto) {
        User user = userRepository.findById(id).orElseThrow();
        Account account = user.getAccount();

        String oldRole = account.getRole().name();
        String oldStatus = account.getStatus().name();

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            account.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        account.setRole(dto.getRole());
        account.setStatus(dto.getStatus());
        accountRepository.save(account);

        mapper.updateUserFromDto(dto, user);

        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId()).orElseThrow();
            user.setCompany(company);
        }

        userRepository.save(user);

        // Ghi Audit Log
        auditLogService.log(
                user.getId(),
                "CẬP NHẬT NGƯỜI DÙNG",
                "Cập nhật tài khoản " + user.getFullName()
                        + " | Vai trò: " + oldRole + " → " + dto.getRole()
                        + " | Trạng thái: " + oldStatus + " → " + dto.getStatus()
        );
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername {}", username);
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));
        log.info("UserDetails : {}", account);

        UserDetails u = org.springframework.security.core.userdetails.User.builder()
                .username(account.getEmail())
                .password(account.getPasswordHash())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + account.getRole().name())))
                .disabled(!account.getStatus().toString().equals("ACTIVE"))
                .build();

        log.info("UserDetails : {}", u);
        return u;
    }
}
