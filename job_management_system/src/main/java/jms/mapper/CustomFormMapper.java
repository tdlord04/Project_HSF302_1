package jms.mapper;

import jms.dto.CustomFormDto;
import jms.entity.CustomForm;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomFormMapper {

    @Mapping(target = "jobPostingId", source = "jobPosting.id")
    @Mapping(target = "jobTitle",    source = "jobPosting.jobTitle")
    CustomFormDto toDto(CustomForm entity);

    @Mapping(target = "jobPosting", ignore = true) // set tại Service
    CustomForm toEntity(CustomFormDto dto);
}