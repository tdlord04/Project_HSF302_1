package jms.controller.job_posting;

import jakarta.validation.Valid;
import jms.dto.JobPostingDto;
import jms.entity.enums.JobStatus;
import jms.service.CompanyService;
import jms.service.CustomFormService;
import jms.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/job-postings/form")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class JobPostingFormController {

    private final JobPostingService jobPostingService;
    private final CompanyService companyService;
    private final CustomFormService customFormService;

    // CREATE
    @GetMapping("/create")
    public String createForm(Model model) {
        if (!model.containsAttribute("job")) {
            model.addAttribute("job", new JobPostingDto());
        }
        model.addAttribute("statuses", JobStatus.values());
        model.addAttribute("companies", companyService.getAll());
        return "job-posting/job-posting-form";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        JobPostingDto dto = jobPostingService.getById(id);
        model.addAttribute("job", dto);
        model.addAttribute("statuses", JobStatus.values());
        model.addAttribute("companies", companyService.getAll());
        return "job-posting/job-posting-form";
    }

    // SAVE
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("job") JobPostingDto dto,
                       BindingResult bindingResult,
                       Model model) {

        // ✅ Nếu có lỗi — giữ nguyên data đã nhập, chỉ hiển thị lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", JobStatus.values());
            model.addAttribute("companies", companyService.getAll());
            return "job-posting/job-posting-form";
        }

        // ✅ Tạo mới
        if (dto.getId() == null) {
            JobPostingDto saved = jobPostingService.create(dto);
            return "redirect:/job-postings/" + saved.getId();
        }

        // ✅ Cập nhật
        jobPostingService.update(dto.getId(), dto);
        return "redirect:/job-postings/" + dto.getId();
    }
}
