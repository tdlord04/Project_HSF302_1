// DocumentRepository.java
package jms.repository;

import jms.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByCandidateId(Long candidateId);

    void deleteByCandidateId(Long candidateId);

    Optional<Document> findByCandidateIdAndDocumentName(Long candidateId, String documentName);

    @Query("SELECT d FROM Document d WHERE d.candidate.id = :candidateId AND d.fileType = :fileType")
    List<Document> findByCandidateIdAndFileType(@Param("candidateId") Long candidateId,
                                                @Param("fileType") String fileType);

    boolean existsByCandidateIdAndDocumentName(Long candidateId, String documentName);

}