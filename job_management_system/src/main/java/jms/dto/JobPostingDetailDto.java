package jms.dto;

import jms.entity.enums.JobStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingDetailDto {

    private Long id;
    private String jobTitle;
    private String description;
    private String requirements;
    private String location;
    private String salaryRange;
    private JobStatus status;

    private Long companyId;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyAddress;
    private String companyWebsite;
}