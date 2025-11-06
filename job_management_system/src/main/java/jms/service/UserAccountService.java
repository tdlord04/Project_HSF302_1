package jms.service;

import jms.dto.UserAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountService {
    Page<UserAccountDTO> findAll(Pageable pageable);
}