package com.example.notificationservice.consumers;

import amq.entities.NotificationMessage;
import com.example.notificationservice.service.NotificationTypeService;
import com.example.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableRabbit
@RequiredArgsConstructor
public class EmailSenderConsumer {

    private final UserService userService;
    private final NotificationTypeService notificationTypeService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.sender.email}")
    private String senderEmail;

    @RabbitListener(queues = "notifications")
    public void sendEmail(@Payload NotificationMessage message) throws InterruptedException {
        List<String> emails = userService.getUsersEmail(message.getMessageType());
        for (int i = 0; i < emails.size(); i++) {
            SimpleMailMessage notification = new SimpleMailMessage();
            notification.setFrom(senderEmail);
            notification.setTo(emails.get(i));
            notification.setSubject(notificationTypeService.getTypeById(message.getMessageType()));
            notification.setText(message.getMessageBody());
            this.javaMailSender.send(notification);
        }
    }
}
