package com.companya.labms.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Lab Reservation System - Password Reset");
        message.setText(
            "Hello,\n\n" +
            "You requested a password reset for your Laboratory Management System (LabMS) account.\n\n" +
            "Click the link below to reset your password:\n" +
            baseUrl + "/reset-password.html?token=" + resetToken + "\n\n" +
            "This link expires in 30 minutes.\n\n" +
            "If you did not request this, ignore this email.\n\n" +
            "Laboratory Management System - Company A"
        );
        mailSender.send(message);
    }

    public void sendEquipmentRequestNotification(String toEmail, String studentName, String itemName, int quantity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Equipment Request - Lab Reservation System");
        message.setText(
            "Hello,\n\n" +
            "A new equipment request has been submitted by " + studentName + ".\n\n" +
            "Requested Item: " + itemName + "\n" +
            "Quantity: " + quantity + "\n\n" +
            "Please log in to the Laboratory Management System to review and approve/reject this request.\n\n" +
            "Laboratory Management System - Company A"
        );
        mailSender.send(message);
    }
}