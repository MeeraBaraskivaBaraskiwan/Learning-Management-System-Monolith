package com.example.project.Notifications;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.project.Notifications.Notification.Notification;
import com.example.project.Notifications.enums.NotificationType;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendNotificationEmail(Notification notification) throws MessagingException {
        String to = notification.getRecipient().getEmail();
        String username = notification.getRecipient().getFirstName();
        NotificationType type = notification.getType();
        logger.info("Preparing to send notification email to: {} for notification type: {}", to, type);

        Context context = new Context();
        context.setVariable("username", username);

        String templateName;
        switch (type) {
            case COURSE_UPDATE -> templateName = "course-update";
            case ASSIGNMENT_DUE -> templateName = "assignment-due";
            case ENROLLMENT_CONFIRMATION -> templateName = "enrollment-confirmation";
            case GRADE_RELEASE -> templateName = "grade-release";
            case NEW_ANNOUNCEMENT -> templateName = "new-announcement";
            case NEW_ASSIGNMENT -> templateName = "new-assignment";
            case INSTRUCTOR_MESSAGE -> templateName = "instructor-message";
            case COURSE_COMPLETION -> templateName = "course-completion";
            case ASSIGNMENT_SUBMISSION -> templateName = "assignment-submission";
            case COURSE_DROPPED -> templateName = "course-dropped";
            case NEW_RESOURCE_ADDED -> templateName = "new-resource-added";
            case UPCOMING_EXAM -> templateName = "upcoming-exam";
            case DEADLINE_REMINDER -> templateName = "deadline-reminder";
            default -> templateName = "generic-notification";
        }

        logger.info("Using email template: {}", templateName);
        String htmlContent = templateEngine.process(templateName, context);

        sendEmail(to, notification.getTitle(), htmlContent);
        logger.info("Notification email sent successfully to: {}", to);
    }

    @Async
    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        logger.info("Preparing email to: {}", to);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("meera.paraskiva@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        logger.info("Email sent successfully to {}", to);
    }

    @Async
    public void sendWelcomeEmail(String to, String firstName) throws MessagingException {
        logger.info("Preparing welcome email for: {}", to);
        Context context = new Context();
        context.setVariable("username", firstName);
        String htmlContent = templateEngine.process("welcome-email", context);
        sendEmail(to, "Welcome to Ctrl+Learn!", htmlContent);
        logger.info("Welcome email sent successfully to: {}", to);
    }

    @Async
    public void sendAdminNotification(String to, String message) throws MessagingException {
        logger.info("Preparing admin notification email to: {}", to);
        Context context = new Context();
        context.setVariable("message", message);
        String htmlContent = templateEngine.process("admin-notification", context);
        sendEmail(to, "New User Registered", htmlContent);
        logger.info("Admin notification email sent successfully to: {}", to);
    }
}