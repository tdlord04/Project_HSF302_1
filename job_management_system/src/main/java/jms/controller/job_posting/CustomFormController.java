package jms.controller.job_posting;

import jakarta.validation.Valid;
import jms.dto.CustomFormDto;
import jms.service.CustomFormService;
import jms.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/custom-forms")
@RequiredArgsConstructor
public class CustomFormController {

    private final CustomFormService customFormService;
    private final JobPostingService jobPostingService;

    // CREATE
    @GetMapping("/create")
    public String create(@RequestParam("jobId") Long jobId, Model model) {
        CustomFormDto dto = CustomFormDto.builder()
                .jobPostingId(jobId)
                .active(true)
                .build();

        model.addAttribute("form", dto);
        // ✅ Luôn set jobTitle từ service, không phụ thuộc DTO
        model.addAttribute("jobTitle", jobPostingService.getById(jobId).getJobTitle());
        return "job-posting/custom-form-builder";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        CustomFormDto dto = customFormService.getById(id);
        model.addAttribute("form", dto);
        // ✅ Với edit thì DTO đã có jobPostingId; lấy lại title từ jobId (đảm bảo không null)
        model.addAttribute("jobTitle", jobPostingService.getById(dto.getJobPostingId()).getJobTitle());
        return "job-posting/custom-form-builder";
    }

    // SAVE (create or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("form") CustomFormDto dto,
                       BindingResult result,
                       Model model) {

        // Nếu có lỗi validate annotation thì trả lại form + nạp jobTitle
        if (result.hasErrors()) {
            model.addAttribute("jobTitle", safeJobTitle(dto.getJobPostingId()));
            return "job-posting/custom-form-builder";
        }

        try {
            CustomFormDto saved = (dto.getId() == null)
                    ? customFormService.create(dto)
                    : customFormService.update(dto.getId(), dto);

            Long jobId = saved.getJobPostingId();
            return "redirect:/job-postings/" + jobId;

        } catch (IllegalArgumentException ex) {
            // ⛑ Service có thể ném lỗi JSON không hợp lệ → đổ vào field 'formStructureJson'
            result.addError(new FieldError("form", "formStructureJson", ex.getMessage()));
        } catch (RuntimeException ex) {
            // Các lỗi khác (ví dụ không tìm thấy Job) → đưa vào global error hoặc cùng field JSON
            result.addError(new FieldError("form", "formStructureJson", "Invalid data: " + ex.getMessage()));
        }

        // ✅ Trả lại view khi lỗi runtime/JSON và vẫn nạp jobTitle để không bị mất
        model.addAttribute("jobTitle", safeJobTitle(dto.getJobPostingId()));
        return "job-posting/custom-form-builder";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam(value = "jobId", required = false) Long jobId) {
        customFormService.deleteById(id);
        // Nếu có jobId → quay lại detail job tương ứng
        if (jobId != null) {
            return "redirect:/job-postings/" + jobId;
        }
        return "redirect:/job-postings";
    }

    private String safeJobTitle(Long jobPostingId) {
        try {
            return jobPostingService.getById(jobPostingId).getJobTitle();
        } catch (Exception e) {
            return "(Unknown Job)";
        }
    }
}
