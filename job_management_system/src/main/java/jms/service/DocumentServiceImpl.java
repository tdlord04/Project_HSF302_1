package jms.service.impl;

import jms.entity.CandidateProfile;
import jms.entity.Document;
import jms.repository.DocumentRepository;
import jms.service.CandidateProfileService;
import jms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final CandidateProfileService candidateProfileService;

    @Override
    @Transactional
    public Document uploadDocumentForCandidate(Long candidateId, MultipartFile file, String documentName) {
        try {
            log.info("Uploading document for candidate {}: {}", candidateId, documentName);

            // Kiểm tra candidate tồn tại
            CandidateProfile candidate = candidateProfileService.getCandidateProfileById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

            // Đọc dữ liệu file vào byte array
            byte[] fileData = file.getBytes();

            // Tạo document entity
            Document document = Document.builder()
                    .candidate(candidate)
                    .documentName(documentName)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileData(fileData) // Lưu trực tiếp vào database
                    .uploadedAt(Instant.now())
                    .build();

            Document savedDocument = documentRepository.save(document);
            log.info("✅ Document saved successfully with ID: {}, Size: {} bytes",
                    savedDocument.getId(), fileData.length);

            return savedDocument;
        } catch (IOException e) {
            log.error("❌ Error reading file data for candidate {}: {}", candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to read file data: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ Error uploading document for candidate {}: {}", candidateId, e.getMessage(), e);
            throw new RuntimeException("Failed to upload document: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Document getDocumentById(Long id) {
        log.info("Getting document by ID: {}", id);
        return documentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("❌ Document not found with ID: {}", id);
                    return new RuntimeException("Document not found with ID: " + id);
                });
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        try {
            log.info("Deleting document with ID: {}", id);

            // Kiểm tra document tồn tại
            Document document = getDocumentById(id);

            // Xóa record trong database
            documentRepository.deleteById(id);
            log.info("✅ Successfully deleted document with id: {}", id);

        } catch (Exception e) {
            log.error("❌ Error deleting document with id: {}", id, e);
            throw new RuntimeException("Failed to delete document: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadDocument(Long id) {
        try {
            log.info("Downloading document with ID: {}", id);

            Document document = getDocumentById(id);

            if (document.getFileData() == null || document.getFileData().length == 0) {
                log.error("❌ Document data is empty for ID: {}", id);
                throw new RuntimeException("Document data is empty");
            }

            log.info("✅ Document data retrieved, size: {} bytes", document.getFileData().length);
            return document.getFileData();

        } catch (Exception e) {
            log.error("❌ Error downloading document with id: {}", id, e);
            throw new RuntimeException("Failed to download document: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByCandidateId(Long candidateId) {
        log.info("Getting documents for candidate ID: {}", candidateId);
        List<Document> documents = documentRepository.findByCandidateId(candidateId);
        log.info("Found {} documents for candidate ID: {}", documents.size(), candidateId);
        return documents;
    }
}