package com.example.itss.service;

import com.example.itss.domain.Email;
import com.example.itss.domain.Schedule;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {
    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendSimpleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("nguyendung30021109@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        this.mailSender.send(msg);
    }


    public void sendScheduleEmail(Email email) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", email.getName());
        context.setVariable("phone", email.getPhone());
        context.setVariable("description", email.getDescription());
        context.setVariable("time", email.getTime());
        context.setVariable("status", "PENDING");

        String process = templateEngine.process("scheduleEmailTemplate", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(email.getEmail());
        helper.setSubject("Schedule Notification");
        helper.setText(process, true);

        javaMailSender.send(mimeMessage);
    }
}
