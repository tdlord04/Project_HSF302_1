// DocumentService.java
package jms.service;

import jms.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Document uploadDocument(Document document);
    Optional<Document> getDocumentById(Long id);
    List<Document> getDocumentsByCandidateId(Long candidateId);
    Document updateDocument(Long id, Document document);
    void deleteDocument(Long id);
    Optional<Document> getDocumentByCandidateAndName(Long candidateId, String documentName);
    List<Document> getDocumentsByCandidateAndFileType(Long candidateId, String fileType);
    boolean documentExistsForCandidate(Long candidateId, String documentName);
    void deleteAllDocumentsByCandidateId(Long candidateId);
}