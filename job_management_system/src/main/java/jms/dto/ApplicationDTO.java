// ApplicationDTO.java
package jms.dto;

import jms.entity.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long jobPostingId;
    private String jobTitle;
    private ApplicationStatus status;
    private String note;
    private String source;
    private Instant createdAt;
    private Instant updatedAt;
}