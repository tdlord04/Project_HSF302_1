package jms.service.impl;

import jms.dto.RecruitmentKpiDto;
import jms.dto.TrendPointDto;
import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import jms.repository.ApplicationRepository;
import jms.service.RecruitmentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentReportServiceImpl implements RecruitmentReportService {

    private final ApplicationRepository applicationRepository;

    @Override
    public RecruitmentKpiDto getKpi(LocalDate start, LocalDate end, Long companyId) {
        Instant startAt = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endAt = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Lấy danh sách đơn ứng tuyển trong khoảng thời gian
        List<Application> all = applicationRepository.findByCreatedAtBetween(startAt, endAt);

        // Lọc theo công ty nếu có
        List<Application> filtered = (companyId == null) ? all :
                all.stream()
                        .filter(a -> a.getJobPosting() != null &&
                                a.getJobPosting().getCompany() != null &&
                                Objects.equals(a.getJobPosting().getCompany().getId(), companyId))
                        .toList();

        long totalCandidates = filtered.size();
        long totalOffers = filtered.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.OFFERED)
                .count();

        // 👉 Tính tỷ lệ trúng tuyển %
        Double offerRate = 0.0;
        if (totalCandidates > 0) {
            offerRate = (totalOffers * 100.0) / totalCandidates;
        }

        return RecruitmentKpiDto.builder()
                .totalCandidates(totalCandidates)
                .totalOffers(totalOffers)
                .averageDaysToOffer(offerRate) // giữ field nhưng dùng để chứa % Offer
                .build();
    }


    @Override
    public List<TrendPointDto> getCandidateTrend(LocalDate start, LocalDate end, Long companyId) {
        Instant startAt = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endAt   = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // exclusive

        List<Application> all = applicationRepository.findByCreatedAtBetween(startAt, endAt);

        // Lọc theo công ty
        List<Application> filtered = (companyId == null) ? all :
                all.stream()
                        .filter(a -> a.getJobPosting() != null &&
                                a.getJobPosting().getCompany() != null &&
                                Objects.equals(a.getJobPosting().getCompany().getId(), companyId))
                        .toList();

        // Gom theo ngày nộp
        Map<LocalDate, Long> grouped = filtered.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        // Bổ sung các ngày trống để line chart liền mạch
        LocalDate d = start;
        while (!d.isAfter(end)) {
            grouped.putIfAbsent(d, 0L);
            d = d.plusDays(1);
        }

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new TrendPointDto(e.getKey(), e.getValue()))
                .toList();
    }
}
