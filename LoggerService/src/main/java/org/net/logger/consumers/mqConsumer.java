package org.net.logger.consumers;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.net.logger.entities.Message;
import org.net.logger.mappers.DocumentMapper;
import org.net.logger.repositories.LogRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class mqConsumer {

    private final DocumentMapper documentMapper;
    private final LogRepository logRepository;

    @RabbitListener(queues = "logs")
    public void processMyQueue(@Payload Message message) throws InterruptedException {
        Document document = documentMapper.toDocument(message.getLogEvent());
        logRepository.store(document, message.getModuleName(), message.getLevel());
    }
}
