// CandidateProfileMapper.java
package jms.mapper;

import jms.dto.CandidateProfileDTO;
import jms.entity.CandidateProfile;
import jms.entity.Skill;
import jms.dto.SkillDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {

    @Mapping(target = "skills", expression = "java(mapSkills(candidateProfile.getSkills()))")
    CandidateProfileDTO toDTO(CandidateProfile candidateProfile);

    CandidateProfile toEntity(CandidateProfileDTO candidateProfileDTO);

    default List<SkillDTO> mapSkills(Set<Skill> skills) {
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