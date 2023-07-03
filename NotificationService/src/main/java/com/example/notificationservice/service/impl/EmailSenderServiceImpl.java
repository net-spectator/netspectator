package com.example.notificationservice.service.impl;

import com.example.notificationservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @Override
    public void sendEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Тестовое письмо");
        message.setText("Тексстовое сообщение в тестовом письме. \nВторая строка.");
        this.javaMailSender.send(message);
    }
}
