package io.github.Bochakms;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetup;

import io.github.Bochakms.exception.NotificationException;
import io.github.Bochakms.service.EmailService;
import io.github.Bochakms.service.UserEventsConsumer;
import io.github.Bochakms.util.EmailTemplates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class NotificationServiceIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP.dynamicPort())
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @DynamicPropertySource
    static void configureMailProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
        registry.add("spring.mail.username", () -> "user");
        registry.add("spring.mail.password", () -> "admin");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "true");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.ssl.enable", () -> "false");
    }

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void resetGreenMail() {
        try {
            greenMail.purgeEmailFromAllMailboxes();
        } catch (FolderException e) {
            greenMail.stop();
            greenMail.start();
        }
    }

    @Test
    void shouldSendAccountCreatedEmail() throws Exception {
        String testEmail = "recipient@example.com";
        emailService.sendAccountCreatedEmail(testEmail);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];
        assertEquals("Account Created", message.getSubject());
        assertEquals(testEmail, message.getAllRecipients()[0].toString());
        assertTrue(message.getContent().toString().contains(EmailTemplates.ACCOUNT_CREATED_TEMPLATE));
    }

    @Test
    void shouldSendAccountDeletedEmail() throws Exception {
        String testEmail = "recipient@example.com";
        emailService.sendAccountDeletedEmail(testEmail);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];
        assertEquals("Account Deleted", message.getSubject());
        assertTrue(message.getContent().toString().contains(EmailTemplates.ACCOUNT_DELETED_TEMPLATE));
    }
}