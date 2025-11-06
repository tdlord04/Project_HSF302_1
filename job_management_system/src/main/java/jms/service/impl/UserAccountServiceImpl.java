package jms.service.impl;

import jms.dto.UserAccountDTO;
import jms.entity.enums.Role;
import jms.entity.enums.UserStatus;
import jms.mapper.UserAccountMapper;
import jms.repository.UserRepository;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final UserAccountMapper mapper;

    @Override
    public Page<UserAccountDTO> findAllFiltered(String roleStr, String statusStr, String keyword, Pageable pageable) {
        Role role = (roleStr == null || roleStr.isEmpty()) ? null : Role.valueOf(roleStr);
        UserStatus status = (statusStr == null || statusStr.isEmpty()) ? null : UserStatus.valueOf(statusStr);

        return userRepository.findAllFiltered(role, status, keyword, pageable)
                .map(mapper::toDTO);
    }


}
