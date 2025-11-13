package jms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCreateDTO {
    private String fullName;
    private String email;
    private String phone;
    private String careerGoal;
    private String education;
    private Integer experienceYears;
    private String previousCompany;
    private String certificates;
    private List<Long> skillIds;
}