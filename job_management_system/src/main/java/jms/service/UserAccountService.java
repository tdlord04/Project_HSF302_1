package jms.service;

import jms.dto.UserAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountService {
    Page<UserAccountDTO> findAllFiltered(String roleStr, String statusStr, String keyword, Pageable pageable);

    void createUser(UserAccountDTO dto);

    void deleteUser(Long id);

}