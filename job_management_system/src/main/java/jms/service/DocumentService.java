package jms.service;

import jms.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    Document uploadDocumentForCandidate(Long candidateId, MultipartFile file, String documentName);
    Document getDocumentById(Long id);
    void deleteDocument(Long id);
    byte[] downloadDocument(Long id);
    List<Document> getDocumentsByCandidateId(Long candidateId);
}