package jms.controller;

import jms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documents")
@Slf4j
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        log.info("=== DOWNLOAD DOCUMENT REQUEST ===");
        log.info("Document ID: {}", id);

        try {
            var document = documentService.getDocumentById(id);
            log.info("Document found: {} - {}", document.getId(), document.getDocumentName());
            log.info("File name: {}, File type: {}", document.getFileName(), document.getFileType());

            byte[] fileData = documentService.downloadDocument(id);
            log.info("File data length: {} bytes", fileData.length);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(document.getFileType()));
            headers.setContentDispositionFormData("attachment", document.getFileName());
            headers.setContentLength(fileData.length);

            log.info("=== DOWNLOAD SUCCESS ===");
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("❌ ERROR downloading document with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<byte[]> previewDocument(@PathVariable Long id) {
        log.info("=== PREVIEW DOCUMENT REQUEST ===");
        log.info("Document ID: {}", id);

        try {
            var document = documentService.getDocumentById(id);
            log.info("Document found: {} - {}", document.getId(), document.getDocumentName());

            byte[] fileData = documentService.downloadDocument(id);
            log.info("File data length: {} bytes", fileData.length);

            HttpHeaders headers = new HttpHeaders();

            // Set content type phù hợp cho preview
            if (document.getFileType().contains("pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else if (document.getFileType().contains("image")) {
                headers.setContentType(MediaType.parseMediaType(document.getFileType()));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            headers.setContentLength(fileData.length);

            log.info("=== PREVIEW SUCCESS ===");
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(" ERROR previewing document with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}