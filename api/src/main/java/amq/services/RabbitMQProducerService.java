package amq.services;

public interface RabbitMQProducerService {
    void sendMessage(Object message, String routingKey);
}
