// InterviewDTO.java
package jms.dto;

import lombok.Data;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class InterviewDTO {
    private Long id;
    private Long applicationId;
    private String candidateName;
    private String jobTitle;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime interviewDate;
    private String location;
    private String result;
    private String notes;
    private Instant createdAt;
    private Instant updatedAt;
}
