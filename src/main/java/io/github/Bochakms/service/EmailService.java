package io.github.Bochakms.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.github.Bochakms.model.UserEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String siteName;

    public void sendNotificationEmail(String toEmail, UserEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        
        if (event == UserEvent.CREATED) {
            message.setSubject("Аккаунт успешно создан");
            message.setText(String.format("Здравствуйте! Ваш аккаунт на сайте %s был успешно создан.", siteName));
        } else if (event == UserEvent.DELETED) {
            message.setSubject("Аккаунт удален");
            message.setText("Здравствуйте! Ваш аккаунт был удалён.");
        }
        
        mailSender.send(message);
    }
}
