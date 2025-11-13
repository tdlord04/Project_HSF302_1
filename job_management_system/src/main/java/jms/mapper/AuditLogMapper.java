package jms.mapper;

import jms.dto.AuditLogDTO;
import jms.entity.AuditLog;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    AuditLogDTO toDTO(AuditLog log);

    @InheritInverseConfiguration
    AuditLog toEntity(AuditLogDTO dto);
}
