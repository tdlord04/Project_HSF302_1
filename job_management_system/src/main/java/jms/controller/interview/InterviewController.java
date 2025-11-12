package jms.controller.interview;

import jms.dto.InterviewDTO;
import jms.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    public String listInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) String interviewer,  // ✅ SỬA THÀNH String
            @RequestParam(required = false) String fromDate,     // ✅ SỬA THÀNH String
            @RequestParam(required = false) String toDate,       // ✅ SỬA THÀNH String
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        // ✅ CONVERT INTERVIEWER
        Long interviewerId = null;
        if (interviewer != null && !interviewer.isEmpty() && !"null".equals(interviewer)) {
            try {
                interviewerId = Long.valueOf(interviewer);
            } catch (NumberFormatException e) {
                // Giữ null nếu convert lỗi
            }
        }

        // ✅ CONVERT DATES
        LocalDate fromDateObj = null;
        LocalDate toDateObj = null;
        try {
            if (fromDate != null && !fromDate.isEmpty() && !"null".equals(fromDate)) {
                fromDateObj = LocalDate.parse(fromDate);
            }
            if (toDate != null && !toDate.isEmpty() && !"null".equals(toDate)) {
                toDateObj = LocalDate.parse(toDate);
            }
        } catch (Exception e) {
            // Giữ null nếu parse lỗi
        }

        Page<InterviewDTO> interviewPage = interviewService.searchInterviews(
                keyword, result, interviewerId, fromDateObj, toDateObj, pageable);

        model.addAttribute("interviewPage", interviewPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("result", result);
        model.addAttribute("interviewer", interviewer);      // GIỮ NGUYÊN STRING
        model.addAttribute("fromDate", fromDate);           // GIỮ NGUYÊN STRING
        model.addAttribute("toDate", toDate);               // ✅ GIỮ NGUYÊN STRING

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", interviewService.countAllInterviews());
        stats.put("pending", interviewService.countInterviewsByResult("PENDING"));
        stats.put("passed", interviewService.countInterviewsByResult("PASS"));
        stats.put("failed", interviewService.countInterviewsByResult("FAIL"));
        model.addAttribute("stats", stats);

        model.addAttribute("interviewers", interviewService.getAllInterviewers());

        return "admin/interviews/list";
    }

    // ... CÁC METHOD KHÁC GIỮ NGUYÊN ...
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("interview", new InterviewDTO());
        model.addAttribute("interviewers", interviewService.getAllInterviewers());
        model.addAttribute("applications", interviewService.getAllApplications());
        return "admin/interviews/create";
    }

    @PostMapping("/create")
    public String createInterview(@ModelAttribute InterviewDTO interviewDTO) {
        interviewService.createInterview(interviewDTO);

        return "redirect:/admin/interviews";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        InterviewDTO interview = interviewService.getInterviewById(id);
        model.addAttribute("interview", interview);
        model.addAttribute("interviewers", interviewService.getAllInterviewers());
        return "admin/interviews/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateInterview(@PathVariable Long id, @ModelAttribute InterviewDTO interviewDTO) {
        interviewService.updateInterview(id, interviewDTO);
        return "redirect:/admin/interviews";
    }

    @GetMapping("/update-result/{id}")
    public String showUpdateResultForm(@PathVariable Long id, Model model) {
        InterviewDTO interview = interviewService.getInterviewById(id);
        model.addAttribute("interview", interview);
        return "admin/interviews/update-result";
    }

    @PostMapping("/update-result/{id}")
    public String updateInterviewResult(@PathVariable Long id,
                                        @RequestParam String result,
                                        @RequestParam(required = false) String notes) {
        interviewService.updateInterviewResult(id, result, notes);
        return "redirect:/admin/interviews";
    }

    @GetMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return "redirect:/admin/interviews";
    }

}