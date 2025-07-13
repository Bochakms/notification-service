package io.github.Bochakms.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import io.github.Bochakms.dto.UserEventMessage;
import io.github.Bochakms.service.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaNotificationConsumer {
    private final EmailService emailService;
    
    @KafkaListener(topics = "user-events")
    public void consume(UserEventMessage message) {
        emailService.sendNotificationEmail(message.getEmail(), message.getEvent());
    }
}
