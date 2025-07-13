package io.github.Bochakms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Bochakms.dto.UserEventMessage;
import io.github.Bochakms.service.EmailService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody UserEventMessage message) {
        emailService.sendNotificationEmail(message.getEmail(), message.getEvent());
        return ResponseEntity.ok("Notification sent successfully");
    }
}
