// ApplicationService.java
package jms.service;

import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application createApplication(Application application);
    Optional<Application> getApplicationById(Long id);
    List<Application> getAllApplications();
    Page<Application> getApplications(Pageable pageable);
    Application updateApplicationStatus(Long id, ApplicationStatus status);
    Application updateApplicationNote(Long id, String note);
    void deleteApplication(Long id);
    List<Application> getApplicationsByCandidateId(Long candidateId);
    List<Application> getApplicationsByJobPostingId(Long jobPostingId);
    List<Application> getApplicationsByStatus(ApplicationStatus status);
    Page<Application> getApplicationsByCandidateId(Long candidateId, Pageable pageable);
    Optional<Application> getApplicationByCandidateAndJobPosting(Long candidateId, Long jobPostingId);
    Long countApplicationsByCandidateId(Long candidateId);
    boolean existsByCandidateAndJobPosting(Long candidateId, Long jobPostingId);
}