package jms.service;

import jms.dto.InterviewDTO;
import jms.entity.Application;
import jms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterviewService {
    // Interview CRUD
    InterviewDTO createInterview(InterviewDTO interviewDTO);
    InterviewDTO getInterviewById(Long id);
    InterviewDTO updateInterview(Long id, InterviewDTO interviewDTO);
    void deleteInterview(Long id);

    // Get interviews - FIXED METHOD NAMES
    List<InterviewDTO> getAllInterviewsList();
    Page<InterviewDTO> getAllInterviews(Pageable pageable);
    Page<InterviewDTO> getAllInterviews(Long interviewerId, Pageable pageable);

    // Filter methods
    List<InterviewDTO> getInterviewsByApplication(Long applicationId);
    List<InterviewDTO> getInterviewsByInterviewer(Long interviewerId);
    List<InterviewDTO> getUpcomingInterviews();

    // Business logic
    InterviewDTO updateInterviewResult(Long id, String result, String notes);

    // Statistics - FIXED RETURN TYPE
    long countAllInterviews();
    long countInterviewsByResult(String result);
    List<Application> getAllApplications();

    Page<InterviewDTO> searchInterviews(String keyword, String result, Long interviewerId,
                                        java.time.LocalDate fromDate, java.time.LocalDate toDate,
                                        Pageable pageable);

    // Utility
    List<User> getAllInterviewers();
}