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

    // ===== DANH SÁCH & BỘ LỌC =====
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

        // Giữ lại bộ lọc khi reload trang
        model.addAttribute("titleFilter", title);
        model.addAttribute("locationFilter", location);
        model.addAttribute("salaryRangeFilter", salaryRange);
        model.addAttribute("statusFilter", status);
        model.addAttribute("requirementsFilter", requirements);
        model.addAttribute("companyNameFilter", companyName);

        return "job-posting/job-posting-list";
    }

    // ===== TẠO MỚI =====
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

    // ===== CẬP NHẬT =====
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("job") JobPostingDto dto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            // Nạp lại danh sách trạng thái nếu có lỗi
            model.addAttribute("statuses", JobStatus.values());
            model.addAttribute("job", dto);
            return "job-posting/job-posting-detail";
        }

        jobPostingService.update(id, dto);
        return "redirect:/job-postings/" + id;
    }

    // ===== XÓA =====
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        jobPostingService.delete(id);
        return "redirect:/job-postings";
    }

    // ===== CHI TIẾT =====
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobPostingService.getDetailById(id));
        model.addAttribute("customForms", customFormService.getByJobPostingId(id));
        model.addAttribute("statuses", JobStatus.values());
        return "job-posting/job-posting-detail";
    }
}
