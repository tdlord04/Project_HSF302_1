package jms.mapper;

import jms.dto.*;
import jms.entity.Company;
import jms.entity.JobPosting;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobPostingMapper {

    // DTO -> ENTITY
    @Mapping(target = "company", expression = "java(mapCompany(dto.getCompanyId()))")
    JobPosting toJobPosting(JobPostingDto dto);

    // ENTITY -> DTO
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    JobPostingDto toDTO(JobPosting jobPosting);

    // ENTITY -> DETAIL DTO
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyEmail", source = "company.email")
    @Mapping(target = "companyPhone", source = "company.phone")
    @Mapping(target = "companyAddress", source = "company.address")
    @Mapping(target = "companyWebsite", source = "company.website")
    JobPostingDetailDto toDetailDTO(JobPosting entity);


    // Helper để map companyId -> Company
    default Company mapCompany(Long companyId) {
        if (companyId == null) return null;
        Company c = new Company();
        c.setId(companyId);
        return c;
    }
}
