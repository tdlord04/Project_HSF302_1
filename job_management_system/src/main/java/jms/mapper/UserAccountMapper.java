package jms.mapper;

import jms.dto.UserAccountDTO;
import jms.entity.Account;
import jms.entity.Company;
import jms.entity.User;
import org.mapstruct.*;

/**
 * Mapper cho CRUD tài khoản người dùng (Admin)
 * Chuyển đổi giữa UserAccountDTO và các entity: Account, User, Company
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper {

    @Mapping(target = "passwordHash", expression = "java(dto.getPassword())")
    Account toAccount(UserAccountDTO dto);

    @Mapping(target = "company", ignore = true) // sẽ set trong service
    User toUser(UserAccountDTO dto);

    // Entity -> DTO (only 1 source, safe)
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.username", target = "username")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.role", target = "role")
    @Mapping(source = "account.status", target = "status")
    @Mapping(source = "account.createdAt", target = "createdAt")
    @Mapping(source = "account.updatedAt", target = "updatedAt")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    UserAccountDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passwordHash", expression = "java(dto.getPassword() != null ? dto.getPassword() : account.getPasswordHash())")
    void updateAccountFromDTO(UserAccountDTO dto, @MappingTarget Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "companyId", target = "company.id")
    void updateUserFromDTO(UserAccountDTO dto, @MappingTarget User user);
}
