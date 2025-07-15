package io.github.Bochakms.controller;

import io.github.Bochakms.dto.EmailRequest;
import io.github.Bochakms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/email")
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendCustomEmail(
                emailRequest.email(),
                emailRequest.subject(),
                emailRequest.text()
        );
    }
}