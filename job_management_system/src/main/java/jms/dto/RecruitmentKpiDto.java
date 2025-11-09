package jms.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecruitmentKpiDto {
    private long totalCandidates;
    private long totalOffers;
    private Double averageDaysToOffer; // null nếu không tính được
}
