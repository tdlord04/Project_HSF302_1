// ApplicationServiceImpl.java
package jms.service.impl;

import jms.entity.Application;
import jms.entity.enums.ApplicationStatus;
import jms.repository.ApplicationRepository;
import jms.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application createApplication(Application application) {
        log.info("Creating application for candidate: {} to job: {}",
                application.getCandidate().getId(), application.getJobPosting().getId());

        if (applicationRepository.existsByCandidateIdAndJobPostingId(
                application.getCandidate().getId(), application.getJobPosting().getId())) {
            throw new RuntimeException("Application already exists for this candidate and job posting");
        }

        return applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Application> getApplicationById(Long id) {
        log.info("Fetching application by id: {}", id);
        return applicationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getAllApplications() {
        log.info("Fetching all applications");
        return applicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getApplications(Pageable pageable) {
        log.info("Fetching applications with pagination");
        return applicationRepository.findAll(pageable);
    }

    @Override
    public Application updateApplicationStatus(Long id, ApplicationStatus status) {
        log.info("Updating application status to: {} for application id: {}", status, id);

        return applicationRepository.findById(id)
                .map(application -> {
                    application.setStatus(status);
                    return applicationRepository.save(application);
                })
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    @Override
    public Application updateApplicationNote(Long id, String note) {
        log.info("Updating application note for application id: {}", id);

        return applicationRepository.findById(id)
                .map(application -> {
                    application.setNote(note);
                    return applicationRepository.save(application);
                })
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    @Override
    public void deleteApplication(Long id) {
        log.info("Deleting application with id: {}", id);
        applicationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByCandidateId(Long candidateId) {
        log.info("Fetching applications for candidate id: {}", candidateId);
        return applicationRepository.findByCandidateId(candidateId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByJobPostingId(Long jobPostingId) {
        log.info("Fetching applications for job posting id: {}", jobPostingId);
        return applicationRepository.findByJobPostingId(jobPostingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getApplicationsByStatus(ApplicationStatus status) {
        log.info("Fetching applications with status: {}", status);
        return applicationRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getApplicationsByCandidateId(Long candidateId, Pageable pageable) {
        log.info("Fetching applications for candidate id: {} with pagination", candidateId);
        return applicationRepository.findByCandidateId(candidateId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Application> getApplicationByCandidateAndJobPosting(Long candidateId, Long jobPostingId) {
        log.info("Fetching application for candidate: {} and job: {}", candidateId, jobPostingId);
        return applicationRepository.findByCandidateAndJobPosting(candidateId, jobPostingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countApplicationsByCandidateId(Long candidateId) {
        log.info("Counting applications for candidate id: {}", candidateId);
        return applicationRepository.countApplicationsByCandidateId(candidateId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCandidateAndJobPosting(Long candidateId, Long jobPostingId) {
        return applicationRepository.existsByCandidateIdAndJobPostingId(candidateId, jobPostingId);
    }
}