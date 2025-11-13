package jms.controller;

import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import jms.service.ApplicationService;
import jms.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/applications")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('HR','ADMIN')")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CandidateProfileService candidateProfileService;

    @GetMapping
    public String listApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) Long candidateId,
            @RequestParam(required = false) Long jobPostingId,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Application> applicationPage;

        if (status != null) {
            List<Application> applications = applicationService.getApplicationsByStatus(status);
            applicationPage = createPageFromList(applications, pageable);
        } else if (candidateId != null) {
            applicationPage = applicationService.getApplicationsByCandidateId(candidateId, pageable);
        } else if (jobPostingId != null) {
            List<Application> applications = applicationService.getApplicationsByJobPostingId(jobPostingId);
            applicationPage = createPageFromList(applications, pageable);
        } else {
            applicationPage = applicationService.getApplications(pageable);
        }

        List<ApplicationStatus> allStatuses = Arrays.asList(ApplicationStatus.values());

        model.addAttribute("applications", applicationPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applicationPage.getTotalPages());
        model.addAttribute("status", status);
        model.addAttribute("candidateId", candidateId);
        model.addAttribute("jobPostingId", jobPostingId);
        model.addAttribute("allStatuses", allStatuses);
        model.addAttribute("currentPath", "/applications");

        return "applications/list";
    }

    // PHƯƠNG THỨC NÀY ĐÃ TỒN TẠI - XÓA PHIÊN BẢN TRÙNG LẶP
    @GetMapping("/{id}")
    public String viewApplication(@PathVariable Long id, Model model) {
        return applicationService.getApplicationById(id)
                .map(application -> {
                    List<ApplicationStatus> allStatuses = Arrays.asList(ApplicationStatus.values());
                    model.addAttribute("application", application);
                    model.addAttribute("allStatuses", allStatuses);
                    model.addAttribute("currentPath", "/applications");
                    return "applications/detail";
                })
                .orElse("redirect:/applications");
    }


    @PostMapping("/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id,
                                          @RequestParam ApplicationStatus status,
                                          RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateApplicationStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái đơn ứng tuyển thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
        return "redirect:/applications/" + id;
    }

    @PostMapping("/{id}/note")
    public String updateApplicationNote(@PathVariable Long id,
                                        @RequestParam String note,
                                        RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateApplicationNote(id, note);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ghi chú thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật ghi chú: " + e.getMessage());
        }
        return "redirect:/applications/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            applicationService.deleteApplication(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa đơn ứng tuyển thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đơn ứng tuyển: " + e.getMessage());
        }
        return "redirect:/applications";
    }

    // Phương thức hỗ trợ tạo Page từ List
    private Page<Application> createPageFromList(List<Application> applications, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), applications.size());

        if (start > applications.size()) {
            return new PageImpl<>(List.of(), pageable, applications.size());
        }

        return new PageImpl<>(applications.subList(start, end), pageable, applications.size());
    }
}