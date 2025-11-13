package jms.service;

import jms.dto.AuditLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    Page<AuditLogDTO> getAll(Pageable pageable);

    AuditLogDTO create(AuditLogDTO dto);

    void log(Long userId, String action, String description);

}
