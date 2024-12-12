package com.example.itss.controller;

import com.example.itss.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
