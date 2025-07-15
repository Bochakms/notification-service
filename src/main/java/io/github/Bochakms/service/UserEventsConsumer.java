package io.github.Bochakms.service;

import io.github.Bochakms.dto.UserEvent;
import io.github.Bochakms.dto.UserEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventsConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handleUserEvent(UserEvent event) {
        log.info("Received user event: {}", event);
        
        try {
            switch (event.eventType()) {
                case CREATED -> emailService.sendAccountCreatedEmail(event.email());
                case DELETED -> emailService.sendAccountDeletedEmail(event.email());
            }
        } catch (Exception e) {
            log.error("Failed to process user event: {}", event, e);
        }
    }
}