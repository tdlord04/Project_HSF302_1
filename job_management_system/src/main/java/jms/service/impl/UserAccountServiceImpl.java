package jms.service.impl;

import jms.dto.UserAccountDTO;
import jms.entity.Account;
import jms.entity.Company;
import jms.entity.User;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.exception.DuplicateFieldException;
import jms.exception.ResourceNotFoundException;
import jms.mapper.UserAccountMapper;
import jms.repository.AccountRepository;
import jms.repository.CompanyRepository;
import jms.repository.UserRepository;
import jms.security.PasswordEncoder;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final UserAccountMapper mapper;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Page<UserAccountDTO> findAllFiltered(String roleStr, String statusStr, String keyword, Pageable pageable) {
        Role role = (roleStr == null || roleStr.isEmpty()) ? null : Role.valueOf(roleStr);
        UserStatus status = (statusStr == null || statusStr.isEmpty()) ? null : UserStatus.valueOf(statusStr);

        return userRepository.findAllFiltered(role, status, keyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public UserAccountDTO createUser(UserAccountDTO dto) {
        // --- 1. Kiểm tra username/email ---
        if (accountRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateFieldException("Tên đăng nhập đã tồn tại: " + dto.getUsername());
        }
        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateFieldException("Email đã tồn tại: " + dto.getEmail());
        }

        // --- 2. Kiểm tra company ---
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công ty có ID " + dto.getCompanyId()));

        // --- 3. Tạo Account ---
        Account account = mapper.toAccount(dto);
        account.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        accountRepository.save(account);

        // --- 4. Tạo User ---
        User user = mapper.toUser(dto);
        user.setAccount(account);
        user.setCompany(company);
        userRepository.save(user);

        // --- 5. Trả DTO ---
        return mapper.toDTO(user);
    }


}
