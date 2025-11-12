// CandidateProfileDTO.java
package jms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String careerGoal;
    private String education;
    private int experienceYears;
    private String previousCompany;
    private String certificates;
    private List<SkillDTO> skills;
    private Instant createdAt;
    private Instant updatedAt;
}