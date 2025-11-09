package jms.controller.job_posting;

import jakarta.validation.Valid;
import jms.dto.JobPostingDto;
import jms.entity.enums.JobStatus;
import jms.service.CustomFormService;
import jms.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/job-postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final CustomFormService customFormService;

    // ===== LIST & FILTER =====
    @GetMapping
    public String list(@RequestParam(value = "title", required = false) String title,
                       @RequestParam(value = "location", required = false) String location,
                       @RequestParam(value = "salaryRange", required = false) String salaryRange,
                       @RequestParam(value = "status", required = false) JobStatus status,
                       @RequestParam(value = "requirements", required = false) String requirements,
                       @RequestParam(value = "companyName", required = false) String companyName,
                       Model model) {

        List<JobPostingDto> postings = jobPostingService.advancedFilter(
                title, location, salaryRange, status, requirements, companyName
        );

        model.addAttribute("postings", postings);
        model.addAttribute("job", new JobPostingDto());
        model.addAttribute("statuses", JobStatus.values());

        // giữ filter
        model.addAttribute("titleFilter", title);
        model.addAttribute("locationFilter", location);
        model.addAttribute("salaryRangeFilter", salaryRange);
        model.addAttribute("statusFilter", status);
        model.addAttribute("requirementsFilter", requirements);
        model.addAttribute("companyNameFilter", companyName);

        return "job-posting/job-posting-list";
    }

    // ===== CREATE =====
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("job") JobPostingDto dto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postings", jobPostingService.getAll());
            model.addAttribute("statuses", JobStatus.values());
            return "job-posting/job-posting-list";
        }

        jobPostingService.create(dto);
        return "redirect:/job-postings";
    }

    // ===== UPDATE =====
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("job") JobPostingDto dto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            // nạp lại danh sách status nếu có lỗi
            model.addAttribute("statuses", JobStatus.values());
            model.addAttribute("job", dto);
            return "job-posting/job-posting-detail";
        }

        jobPostingService.update(id, dto);
        return "redirect:/job-postings/" + id;
    }

    // ===== DELETE =====
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        jobPostingService.delete(id);
        return "redirect:/job-postings";
    }

    // ===== DETAIL =====
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobPostingService.getDetailById(id));
        model.addAttribute("customForms", customFormService.getByJobPostingId(id));
        model.addAttribute("statuses", JobStatus.values());
        return "job-posting/job-posting-detail";
    }
}
