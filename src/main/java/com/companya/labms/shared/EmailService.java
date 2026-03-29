package com.companya.labms.shared;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Lab Reservation System - Password Reset");
        message.setText(
            "Hello,\n\n" +
            "You requested a password reset for your Lab Reservation System account.\n\n" +
            "Click the link below to reset your password:\n" +
            "http://localhost:8080/reset-password.html?token=" + resetToken + "\n\n" +
            "This link expires in 30 minutes.\n\n" +
            "If you did not request this, ignore this email.\n\n" +
            "Lab Reservation System"
        );
        mailSender.send(message);
    }
}