package jms.service.impl;

import jms.dto.UserAccountDTO;
import jms.entity.User;
import jms.mapper.UserAccountMapper;
import jms.repository.UserRepository;
import jms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final UserAccountMapper mapper;

    @Override
    public Page<UserAccountDTO> findAll(Pageable pageable) {
        return userRepository.findAllByDeletedAtIsNull(pageable)
                .map(mapper::toDTO);
    }
}
