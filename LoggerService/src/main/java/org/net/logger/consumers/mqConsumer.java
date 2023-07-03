package org.net.logger.consumers;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.net.logger.entities.Message;
import org.net.logger.mappers.DocumentMapper;
import org.net.logger.services.LoggerService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class mqConsumer {

    private final DocumentMapper documentMapper;
    private final LoggerService loggerService;

    @RabbitListener(queues = "logs")
    public void processMyQueue(@Payload Message message) throws InterruptedException {
        Document document = documentMapper.toDocument(message.getLogEvent());
        loggerService.store(message.getLogTime(), document, message.getModuleName(), message.getLevel());
    }
}
