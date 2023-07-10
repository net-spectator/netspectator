package ru.larionov.inventoryservice.services;

import amq.entities.NotificationMessage;
import amq.services.RabbitMQProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitMQProducerService mqService;

    private static final String NOTIFICATION_KEY = "nsNotifications2311";

    public void sendNotification(Long typeMessage, String message) {
        mqService.sendMessage(new NotificationMessage(typeMessage, message), NOTIFICATION_KEY);
    }
}
