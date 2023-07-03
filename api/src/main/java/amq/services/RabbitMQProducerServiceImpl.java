package amq.services;

import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Autowired
    public RabbitMQProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object message, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}