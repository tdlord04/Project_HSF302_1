package jms.service.impl;

import jms.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Simple email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}, error: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendInterviewInvitation(String candidateEmail, String candidateName,
                                        String jobTitle, String interviewerName,
                                        String interviewDate, String location, String notes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("jobTitle", jobTitle);
            context.setVariable("interviewerName", interviewerName);
            context.setVariable("interviewDate", interviewDate);
            context.setVariable("location", location);
            context.setVariable("notes", notes);
            context.setVariable("companyName", "NextGen HR Solutions");

            String htmlContent = templateEngine.process("email/interview-invitation", context);

            helper.setTo(candidateEmail);
            helper.setSubject("Thư mời phỏng vấn - " + jobTitle);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Interview invitation email sent successfully to: {}", candidateEmail);
        } catch (MessagingException e) {
            log.error("Failed to send interview invitation email to: {}, error: {}", candidateEmail, e.getMessage());
            throw new RuntimeException("Failed to send interview invitation email", e);
        }
    }

    @Override
    public void sendInterviewUpdate(String candidateEmail, String candidateName,
                                    String jobTitle, String interviewerName,
                                    String interviewDate, String location, String notes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("jobTitle", jobTitle);
            context.setVariable("interviewerName", interviewerName);
            context.setVariable("interviewDate", interviewDate);
            context.setVariable("location", location);
            context.setVariable("notes", notes);
            context.setVariable("companyName", "NextGen HR Solutions");

            String htmlContent = templateEngine.process("email/interview-update", context);

            helper.setTo(candidateEmail);
            helper.setSubject("Thông báo thay đổi lịch phỏng vấn - " + jobTitle);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Interview update email sent successfully to: {}", candidateEmail);
        } catch (MessagingException e) {
            log.error("Failed to send interview update email to: {}, error: {}", candidateEmail, e.getMessage());
            throw new RuntimeException("Failed to send interview update email", e);
        }
    }

    @Override
    public void sendInterviewResult(String candidateEmail, String candidateName,
                                    String jobTitle, String result, String notes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("jobTitle", jobTitle);
            context.setVariable("result", result);
            context.setVariable("notes", notes);
            context.setVariable("companyName", "NextGen HR Solutions");

            String htmlContent = templateEngine.process("email/interview-result", context);

            helper.setTo(candidateEmail);
            helper.setSubject("Thông báo kết quả phỏng vấn - " + jobTitle);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Interview result email sent successfully to: {}", candidateEmail);
        } catch (MessagingException e) {
            log.error("Failed to send interview result email to: {}, error: {}", candidateEmail, e.getMessage());
            throw new RuntimeException("Failed to send interview result email", e);
        }
    }

    @Override
    public void sendInterviewReminder(String candidateEmail, String candidateName,
                                      String jobTitle, String interviewDate, String location) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("candidateName", candidateName);
            context.setVariable("jobTitle", jobTitle);
            context.setVariable("interviewDate", interviewDate);
            context.setVariable("location", location);
            context.setVariable("companyName", "NextGen HR Solutions");

            String htmlContent = templateEngine.process("email/interview-reminder", context);

            helper.setTo(candidateEmail);
            helper.setSubject("Nhắc lịch phỏng vấn - " + jobTitle);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Interview reminder email sent successfully to: {}", candidateEmail);
        } catch (MessagingException e) {
            log.error("Failed to send interview reminder email to: {}, error: {}", candidateEmail, e.getMessage());
            throw new RuntimeException("Failed to send interview reminder email", e);
        }
    }

}