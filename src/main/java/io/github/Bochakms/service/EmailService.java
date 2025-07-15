package io.github.Bochakms.service;

import io.github.Bochakms.exception.NotificationException;
import io.github.Bochakms.util.EmailTemplates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	public String mailFrom;
	
    @Value("${spring.mail.port}")
    private int mailPort;

    public void sendAccountCreatedEmail(String email) {
        sendEmail(email, 
                "Account Created", 
                EmailTemplates.ACCOUNT_CREATED_TEMPLATE);
    }

    public void sendAccountDeletedEmail(String email) {
        sendEmail(email, 
                "Account Deleted", 
                EmailTemplates.ACCOUNT_DELETED_TEMPLATE);
    }

    public void sendCustomEmail(String email, String subject, String text) {
        sendEmail(email, subject, text);
    }

    private void sendEmail(String email, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Email sent to {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to {}", email, e);
            throw new NotificationException("Failed to send email", e);
        }
    }
}