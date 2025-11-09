package jms.controller.job_posting;

import jms.dto.RecruitmentKpiDto;
import jms.dto.TrendPointDto;
import jms.service.CompanyService;
import jms.service.RecruitmentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/recruitment-report")
@RequiredArgsConstructor
public class RecruitmentReportController {

    private final RecruitmentReportService recruitmentReportService;
    private final CompanyService companyService;

    @GetMapping
    public String dashboard(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) Long companyId,
            Model model
    ) {
        LocalDate startDate = (start != null && !start.isEmpty()) ? LocalDate.parse(start) : LocalDate.now().minusMonths(1);
        LocalDate endDate = (end != null && !end.isEmpty()) ? LocalDate.parse(end) : LocalDate.now();

        RecruitmentKpiDto kpi = recruitmentReportService.getKpi(startDate, endDate, companyId);
        List<TrendPointDto> trend = recruitmentReportService.getCandidateTrend(startDate, endDate, companyId);

        // Map trend data for chart
        List<String> labels = trend.stream()
                .map(t -> t.getDate().toString())
                .toList();
        List<Long> counts = trend.stream()
                .map(TrendPointDto::getCount)
                .toList();

        System.out.println("TREND LABELS: " + labels);
        System.out.println("TREND COUNTS: " + counts);
        System.out.println("TREND KPIS: " + kpi);

        model.addAttribute("kpi", kpi);
        model.addAttribute("trendLabels", labels);
        model.addAttribute("trendCounts", counts);
        model.addAttribute("companies", companyService.getAll());
        model.addAttribute("req", new ReportFilterRequest(startDate.toString(), endDate.toString(), companyId));

        return "job-posting/recruitment-dashboard";
    }

    // Inner record to remember filters
    record ReportFilterRequest(String start, String end, Long companyId) {}
}
