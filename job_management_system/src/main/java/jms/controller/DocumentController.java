// DocumentController.java
package jms.controller;

import jms.entity.Document;
import jms.service.DocumentService;
import jms.service.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/documents")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','HR')")
public class DocumentController {

    private final DocumentService documentService;
    private final CandidateProfileService candidateProfileService;

    @GetMapping
    public String listDocuments(
            @RequestParam(required = false) Long candidateId,
            @RequestParam(required = false) String fileType,
            Model model) {

        List<Document> documents;

        if (candidateId != null && fileType != null) {
            documents = documentService.getDocumentsByCandidateAndFileType(candidateId, fileType);
        } else if (candidateId != null) {
            documents = documentService.getDocumentsByCandidateId(candidateId);
        } else {
            // In real application, you might want pagination here
            documents = List.of(); // Or implement getAllDocuments method
        }

        model.addAttribute("documents", documents);
        model.addAttribute("candidateId", candidateId);
        model.addAttribute("fileType", fileType);
        model.addAttribute("currentPath", "/documents");

        return "documents/list";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("candidates", candidateProfileService.getAllCandidateProfiles());
        model.addAttribute("currentPath", "/documents");
        return "documents/upload";
    }

    @PostMapping("/upload")
    public String uploadDocument(@ModelAttribute Document document,
                                 RedirectAttributes redirectAttributes) {
        try {
            documentService.uploadDocument(document);
            redirectAttributes.addFlashAttribute("successMessage", "Upload tài liệu thành công!");
            return "redirect:/documents";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi upload tài liệu: " + e.getMessage());
            return "redirect:/documents/upload";
        }
    }

    @GetMapping("/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        return documentService.getDocumentById(id)
                .map(document -> {
                    model.addAttribute("document", document);
                    model.addAttribute("currentPath", "/documents");
                    return "documents/detail";
                })
                .orElse("redirect:/documents");
    }

    @PostMapping("/{id}/delete")
    public String deleteDocument(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            documentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa tài liệu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa tài liệu: " + e.getMessage());
        }
        return "redirect:/documents";
    }
}