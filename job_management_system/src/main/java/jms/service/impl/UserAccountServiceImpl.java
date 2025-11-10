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
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
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

    @Override
    public Page<UserAccountDTO> findAllFiltered(String roleStr, String statusStr, String keyword, Pageable pageable) {
        Role role = (roleStr == null || roleStr.isEmpty()) ? null : Role.valueOf(roleStr);
        UserStatus status = (statusStr == null || statusStr.isEmpty()) ? null : UserStatus.valueOf(statusStr);

        return userRepository.findAllFiltered(role, status, keyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public void createUser(UserAccountDTO dto) {
        if (accountRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateFieldException("Tên đăng nhập đã tồn tại: " + dto.getUsername());
        }
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

        mapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new RuntimeException("User not found"));
        Account account = user.getAccount();
        account.setDeletedAt(Instant.now());
        user.setDeletedAt(Instant.now());

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername {}", username);
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));
        log.info("UserDetails : {}", account);

        // Thêm ROLE_ prefix nếu bạn dùng .hasRole("ADMIN") trong SecurityConfig
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
