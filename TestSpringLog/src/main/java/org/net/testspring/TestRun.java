package org.net.testspring;

import amq.entities.NotificationMessage;
import amq.services.RabbitMQProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import utils.ModuleName;
import utils.NSLogger;

import javax.annotation.PostConstruct;

@Component
@ComponentScan(basePackages = {"amq"})
@RequiredArgsConstructor
public class TestRun {

    private final RabbitMQProducerService rabbitMQProducerService;
    @Autowired
    private ApplicationContext context;

    @Value("${spring.rabbitmq.template.logs-routing-key}")
    private String logsRoutingKey;

    @Value("${spring.rabbitmq.template.notifivation-routing-key}")
    private String notificationsRoutingKey;

    @PostConstruct
    public void init() {
        ModuleName.getModuleName().setName("TestSpring");
        NSLogger logger = new NSLogger(TestSpringApplication.class, context, logsRoutingKey);
        logger.setLocalLog(false);
        logger.setServerLog(true);
        logger.info("info message");
        logger.debug("debug message");
        logger.error("error message");
        NotificationMessage message = new NotificationMessage(1L, "test message");
        rabbitMQProducerService.sendMessage(message, notificationsRoutingKey);
    }
}
