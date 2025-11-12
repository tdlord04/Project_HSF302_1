// jms/dto/RecruitmentReportRequest.java
package jms.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecruitmentReportRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate end;

    private Long companyId; // optional filter
}

