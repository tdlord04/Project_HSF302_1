// CandidateProfileMapper.java
package jms.mapper;

import jms.dto.CandidateProfileDTO;
import jms.entity.CandidateProfile;
import jms.entity.Skill;
import jms.dto.SkillDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {

    @Mapping(target = "skills", source = "skills", qualifiedByName = "mapSkills")
    CandidateProfileDTO toDTO(CandidateProfile candidateProfile);

    CandidateProfile toEntity(CandidateProfileDTO candidateProfileDTO);

    @Named("mapSkills")
    default List<SkillDTO> mapSkills(List<Skill> skills) {
        if (skills == null) {
            return List.of();
        }
        return skills.stream()
                .map(skill -> SkillDTO.builder()
                        .id(skill.getId())
                        .name(skill.getName())
                        .build())
                .collect(Collectors.toList());
    }
}