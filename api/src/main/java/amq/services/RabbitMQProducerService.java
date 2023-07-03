package amq.services;

import java.time.LocalDateTime;

public interface RabbitMQProducerService {
    void sendMessage(Object message, String routingKey);
}
