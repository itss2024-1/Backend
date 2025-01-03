package com.example.itss.controller;

import com.example.itss.domain.Email;
import com.example.itss.domain.Schedule;
import com.example.itss.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/simple")
    public String sendEmail() {
        this.emailService.sendSimpleEmail();
        return "Email sent successfully";
    }

    @PostMapping("/schedule")
    public String sendScheduleEmail( @RequestBody Email email) {
        try {
            emailService.sendScheduleEmail(email);
            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }

}
