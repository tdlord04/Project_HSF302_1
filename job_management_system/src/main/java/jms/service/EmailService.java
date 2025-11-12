package jms.service;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

    void sendInterviewInvitation(String candidateEmail, String candidateName,
                                 String jobTitle, String interviewerName,
                                 String interviewDate, String location, String notes);

    void sendInterviewUpdate(String candidateEmail, String candidateName,
                             String jobTitle, String interviewerName,
                             String interviewDate, String location, String notes);

    void sendInterviewResult(String candidateEmail, String candidateName,
                             String jobTitle, String result, String notes);

    void sendInterviewReminder(String candidateEmail, String candidateName,
                               String jobTitle, String interviewDate, String location);
}