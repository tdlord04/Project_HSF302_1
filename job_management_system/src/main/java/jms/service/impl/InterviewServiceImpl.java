package jms.service.impl;

import jms.dto.InterviewDTO;
import jms.entity.Application;
import jms.entity.Interview;
import jms.entity.User;
import jms.entity.enums.ApplicationStatus;
import jms.mapper.InterviewMapper;
import jms.repository.ApplicationRepository;
import jms.repository.InterviewRepository;
import jms.repository.UserRepository;
import jms.service.EmailService;
import jms.service.InterviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j  // Thêm annotation @Slf4j để sử dụng log
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final InterviewMapper interviewMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public InterviewDTO createInterview(InterviewDTO interviewDTO) {
        validateInterview(interviewDTO);

        Interview interview = interviewMapper.toEntity(interviewDTO);
        setInterviewRelationships(interview, interviewDTO);

        Interview savedInterview = interviewRepository.save(interview);
        updateApplicationStatus(interviewDTO.getApplicationId(), ApplicationStatus.INTERVIEWING);
        sendInterviewInvitationEmail(savedInterview);

        return interviewMapper.toDTO(savedInterview);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewDTO getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
        return interviewMapper.toDTO(interview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDTO> getAllInterviewsList() {
        return interviewRepository.findAll().stream()
                .map(interviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewDTO> getAllInterviews(Pageable pageable) {
        return interviewRepository.findAll(pageable)
                .map(interviewMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewDTO> getAllInterviews(Long interviewerId, Pageable pageable) {
        if (interviewerId != null) {
            return interviewRepository.findByInterviewerId(interviewerId, pageable)
                    .map(interviewMapper::toDTO);
        } else {
            return interviewRepository.findAll(pageable)
                    .map(interviewMapper::toDTO);
        }
    }

    @Override
    @Transactional
    public InterviewDTO updateInterview(Long id, InterviewDTO interviewDTO) {
        Interview existingInterview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));

        // Lưu thông tin cũ để so sánh
        LocalDateTime oldInterviewDate = existingInterview.getInterviewDate();
        String oldLocation = existingInterview.getLocation();
        User oldInterviewer = existingInterview.getInterviewer();

        validateInterview(interviewDTO);
        interviewMapper.updateInterviewFromDTO(interviewDTO, existingInterview);
        setInterviewRelationships(existingInterview, interviewDTO);

        Interview updatedInterview = interviewRepository.save(existingInterview);

        // Gửi email cập nhật nếu có thay đổi quan trọng
        if (isSignificantChange(oldInterviewDate, oldLocation, oldInterviewer, updatedInterview)) {
            sendInterviewUpdateEmail(updatedInterview);
        }

        return interviewMapper.toDTO(updatedInterview);
    }

    @Override
    @Transactional
    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));
        try {
            // Cách 1: Sử dụng method trong InterviewRepository (nếu đã thêm)
            if (interviewRepository.existsEvaluationNotesByInterviewId(id)) {
                interviewRepository.deleteEvaluationNotesByInterviewId(id);
                log.info("✅ Đã xóa evaluation notes cho interview ID: {}", id);
            }
        } catch (Exception e) {
            log.warn("Không thể xóa evaluation notes cho interview ID: {}, có thể method chưa được implement", id);
        }
        interviewRepository.delete(interview);
        log.info("✅ Đã xóa lịch phỏng vấn ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getAllApplications() {
        return applicationRepository.findAllWithCandidateAndJobPosting();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDTO> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId).stream()
                .map(interviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDTO> getInterviewsByInterviewer(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId).stream()
                .map(interviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDTO> getUpcomingInterviews() {
        // Chuyển Instant sang LocalDateTime để so sánh
        LocalDateTime now = LocalDateTime.now();
        return interviewRepository.findByInterviewDateAfter(now).stream()
                .map(interviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public InterviewDTO updateInterviewResult(Long id, String result, String notes) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));

        String oldResult = interview.getResult();
        interview.setResult(result);
        if (notes != null) {
            interview.setNotes(notes);
        }

        Interview updatedInterview = interviewRepository.save(interview);

        // Gửi email thông báo kết quả nếu kết quả thay đổi
        if (!result.equals(oldResult)) {
            sendInterviewResultEmail(updatedInterview, result, notes);
        }

        return interviewMapper.toDTO(updatedInterview);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllInterviews() {
        return interviewRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countInterviewsByResult(String result) {
        return interviewRepository.countByResult(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewDTO> searchInterviews(String keyword, String result, Long interviewerId,
                                               java.time.LocalDate fromDate, java.time.LocalDate toDate,
                                               Pageable pageable) {
        return interviewRepository.searchInterviews(keyword, result, interviewerId, fromDate, toDate, pageable)
                .map(interviewMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllInterviewers() {
        return userRepository.findByAccount_Role(jms.entity.enums.Role.INTERVIEWER);
    }

    // ========== PRIVATE HELPER METHODS ==========

    private void setInterviewRelationships(Interview interview, InterviewDTO interviewDTO) {
        Application application = applicationRepository.findById(interviewDTO.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + interviewDTO.getApplicationId()));
        interview.setApplication(application);

        if (interviewDTO.getInterviewerId() != null) {
            User interviewer = userRepository.findById(interviewDTO.getInterviewerId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + interviewDTO.getInterviewerId()));
            interview.setInterviewer(interviewer);
        } else {
            interview.setInterviewer(null);
        }
    }

    private void validateInterview(InterviewDTO interviewDTO) {
        if (interviewDTO.getInterviewDate() == null) {
            throw new RuntimeException("Interview date is required");
        }

        if (interviewDTO.getInterviewDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Interview date cannot be in the past");
        }

        if (interviewDTO.getApplicationId() == null) {
            throw new RuntimeException("Application ID is required");
        }

        if (interviewDTO.getResult() != null) {
            String result = interviewDTO.getResult().toUpperCase();
            if (!result.equals("PASS") && !result.equals("FAIL") && !result.equals("PENDING")) {
                throw new RuntimeException("Result must be PASS, FAIL, or PENDING");
            }
            interviewDTO.setResult(result);
        } else {
            interviewDTO.setResult("PENDING");
        }
    }

    private void updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));
        application.setStatus(status);
        applicationRepository.save(application);
    }

    // ========== EMAIL METHODS ==========

    private void sendInterviewInvitationEmail(Interview interview) {
        try {
            String candidateEmail = interview.getApplication().getCandidate().getEmail();
            String candidateName = interview.getApplication().getCandidate().getFullName();
            String jobTitle = interview.getApplication().getJobPosting().getJobTitle();
            String interviewerName = interview.getInterviewer() != null ?
                    interview.getInterviewer().getFullName() : "HR Team";

            // Format date for better display
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String interviewDate = interview.getInterviewDate().format(formatter);

            String location = interview.getLocation() != null ? interview.getLocation() : "Online";
            String notes = interview.getNotes();

            emailService.sendInterviewInvitation(
                    candidateEmail, candidateName, jobTitle, interviewerName,
                    interviewDate, location, notes
            );

            log.info("Interview invitation email sent successfully to: {}", candidateEmail);

        } catch (Exception e) {
            // Log lỗi nhưng không throw để không ảnh hưởng đến việc tạo interview
            log.error("Failed to send interview invitation email for candidate: {}, error: {}",
                    interview.getApplication().getCandidate().getFullName(), e.getMessage());
        }
    }

    /**
     * Gửi email kết quả phỏng vấn
     */
    private void sendInterviewResultEmail(Interview interview, String result, String notes) {
        try {
            String candidateEmail = interview.getApplication().getCandidate().getEmail();
            String candidateName = interview.getApplication().getCandidate().getFullName();
            String jobTitle = interview.getApplication().getJobPosting().getJobTitle();

            emailService.sendInterviewResult(
                    candidateEmail, candidateName, jobTitle, result, notes
            );

            log.info("Interview result email sent successfully to: {}", candidateEmail);

        } catch (Exception e) {
            log.error("Failed to send interview result email for candidate: {}, error: {}",
                    interview.getApplication().getCandidate().getFullName(), e.getMessage());
        }
    }

    /**
     * Gửi email cập nhật lịch phỏng vấn
     */
    private void sendInterviewUpdateEmail(Interview interview) {
        try {
            String candidateEmail = interview.getApplication().getCandidate().getEmail();
            String candidateName = interview.getApplication().getCandidate().getFullName();
            String jobTitle = interview.getApplication().getJobPosting().getJobTitle();
            String interviewerName = interview.getInterviewer() != null ?
                    interview.getInterviewer().getFullName() : "HR Team";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String interviewDate = interview.getInterviewDate().format(formatter);

            String location = interview.getLocation() != null ? interview.getLocation() : "Online";
            String notes = interview.getNotes();

            emailService.sendInterviewUpdate(
                    candidateEmail, candidateName, jobTitle, interviewerName,
                    interviewDate, location, notes
            );

            log.info("Interview update email sent successfully to: {}", candidateEmail);

        } catch (Exception e) {
            log.error("Failed to send interview update email for candidate: {}, error: {}",
                    interview.getApplication().getCandidate().getFullName(), e.getMessage());
        }
    }

    /**
     * Kiểm tra xem có thay đổi quan trọng không
     */
    private boolean isSignificantChange(LocalDateTime oldDate, String oldLocation, User oldInterviewer, Interview newInterview) {
        return !oldDate.equals(newInterview.getInterviewDate()) ||
                !equals(oldLocation, newInterview.getLocation()) ||
                !equals(oldInterviewer, newInterview.getInterviewer());
    }

    /**
     * So sánh an toàn với null
     */
    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) return true;
        if (obj1 == null || obj2 == null) return false;
        return obj1.equals(obj2);
    }
}