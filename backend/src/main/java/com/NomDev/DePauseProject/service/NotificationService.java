package com.NomDev.DePauseProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void sendSupportEmail(String email, String problemType, String description) {
        String subject = "Support Request: " + problemType;
        String body = "You have received a new support request.\n\n"
                + "From: " + email + "\n"
                + "Problem Type: " + problemType + "\n"
                + "Description:\n" + description;
        sendEmail("noreplydepause@gmail.com", subject, body);
    }
}

