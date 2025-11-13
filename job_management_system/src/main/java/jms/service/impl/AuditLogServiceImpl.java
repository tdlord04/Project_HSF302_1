package jms.service.impl;

import jms.dto.AuditLogDTO;
import jms.entity.AuditLog;
import jms.entity.User;
import jms.mapper.AuditLogMapper;
import jms.repository.AuditLogRepository;
import jms.repository.UserRepository;
import jms.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    @Override
    public Page<AuditLogDTO> getAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(auditLogMapper::toDTO);
    }

    @Override
    public AuditLogDTO create(AuditLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuditLog log = AuditLog.builder()
                .user(user)
                .action(dto.getAction())
                .description(dto.getDescription())
                .timestamp(Instant.now())
                .build();

        return auditLogMapper.toDTO(auditLogRepository.save(log));
    }

    @Override
    public void log(Long userId, String action, String description) {
        AuditLogDTO dto = AuditLogDTO.builder()
                .userId(userId)
                .action(action)
                .description(description)
                .timestamp(Instant.now())
                .build();
        create(dto);
    }

}
