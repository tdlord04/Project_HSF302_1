// DocumentServiceImpl.java
package jms.service.impl;

import jms.entity.Document;
import jms.repository.DocumentRepository;
import jms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public Document uploadDocument(Document document) {
        log.info("Uploading document: {} for candidate: {}",
                document.getDocumentName(), document.getCandidate().getId());

        if (documentRepository.existsByCandidateIdAndDocumentName(
                document.getCandidate().getId(), document.getDocumentName())) {
            throw new RuntimeException("Document with name '" + document.getDocumentName() +
                    "' already exists for this candidate");
        }

        return documentRepository.save(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> getDocumentById(Long id) {
        log.info("Fetching document by id: {}", id);
        return documentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByCandidateId(Long candidateId) {
        log.info("Fetching documents for candidate id: {}", candidateId);
        return documentRepository.findByCandidateId(candidateId);
    }

    @Override
    public Document updateDocument(Long id, Document document) {
        log.info("Updating document with id: {}", id);

        return documentRepository.findById(id)
                .map(existingDocument -> {
                    existingDocument.setDocumentName(document.getDocumentName());
                    existingDocument.setFilePath(document.getFilePath());
                    existingDocument.setFileType(document.getFileType());
                    return documentRepository.save(existingDocument);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    @Override
    public void deleteDocument(Long id) {
        log.info("Deleting document with id: {}", id);
        documentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> getDocumentByCandidateAndName(Long candidateId, String documentName) {
        log.info("Fetching document by name: {} for candidate: {}", documentName, candidateId);
        return documentRepository.findByCandidateIdAndDocumentName(candidateId, documentName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByCandidateAndFileType(Long candidateId, String fileType) {
        log.info("Fetching documents of type: {} for candidate: {}", fileType, candidateId);
        return documentRepository.findByCandidateIdAndFileType(candidateId, fileType);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean documentExistsForCandidate(Long candidateId, String documentName) {
        return documentRepository.existsByCandidateIdAndDocumentName(candidateId, documentName);
    }

    @Override
    public void deleteAllDocumentsByCandidateId(Long candidateId) {
        log.info("Deleting all documents for candidate id: {}", candidateId);
        documentRepository.deleteByCandidateId(candidateId);
    }
}