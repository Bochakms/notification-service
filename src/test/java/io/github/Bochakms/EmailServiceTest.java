package io.github.Bochakms;

import io.github.Bochakms.exception.NotificationException;
import io.github.Bochakms.service.EmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    
    @Mock
    private JavaMailSender mailSender;
    
    @InjectMocks
    private EmailService emailService;
    
    private final String testEmail = "test@example.com";
    private final String mailFrom = "noreply@example.com";

    @Test
    void sendAccountCreatedEmail_ShouldSendEmailSuccessfully() {
        // Arrange
        emailService.mailFrom = mailFrom;
        
        // Act
        emailService.sendAccountCreatedEmail(testEmail);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void sendAccountDeletedEmail_ShouldSendEmailSuccessfully() {
        // Arrange
        emailService.mailFrom = mailFrom;
        
        // Act
        emailService.sendAccountDeletedEmail(testEmail);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void sendCustomEmail_ShouldSendEmailSuccessfully() {
        // Arrange
        emailService.mailFrom = mailFrom;
        String subject = "Test Subject";
        String text = "Test Text";
        
        // Act
        emailService.sendCustomEmail(testEmail, subject, text);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void sendEmail_ShouldThrowNotificationException_WhenSendingFails() {
        // Arrange
        emailService.mailFrom = mailFrom;
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act & Assert
        assertThrows(NotificationException.class, () -> {
            emailService.sendCustomEmail(testEmail, "Subject", "Text");
        });
    }
    
    @Test
    void sendEmail_ShouldSetCorrectMessageProperties() {
        // Arrange
        emailService.mailFrom = mailFrom;
        String subject = "Test Subject";
        String text = "Test Text";
        
        // Act
        emailService.sendCustomEmail(testEmail, subject, text);
        
        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(mailFrom, sentMessage.getFrom());
        assertEquals(testEmail, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(text, sentMessage.getText());
    }
}
