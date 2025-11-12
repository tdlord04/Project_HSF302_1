package jms.service;

import jms.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface RecruitmentReportService {
    RecruitmentKpiDto getKpi(LocalDate start, LocalDate end, Long companyId);
    List<TrendPointDto> getCandidateTrend(LocalDate start, LocalDate end, Long companyId);
}