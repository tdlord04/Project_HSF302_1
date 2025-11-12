// ApplicationMapper.java
package jms.mapper;

import jms.dto.ApplicationDTO;
import jms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(target = "candidateId", source = "candidate.id")
    @Mapping(target = "candidateName", source = "candidate.fullName")
    @Mapping(target = "jobPostingId", source = "jobPosting.id")
    @Mapping(target = "jobTitle", source = "jobPosting.jobTitle") // Sửa thành jobTitle
    ApplicationDTO toDTO(Application application);

    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "jobPosting", ignore = true)
    Application toEntity(ApplicationDTO applicationDTO);
}