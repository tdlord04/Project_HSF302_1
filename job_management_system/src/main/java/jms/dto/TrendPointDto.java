package jms.dto;

import lombok.*;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TrendPointDto {
    private LocalDate date;
    private long count;
}
