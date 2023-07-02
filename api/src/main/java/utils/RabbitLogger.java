package utils;

import amq.entities.LogEvent;
import amq.entities.Message;
import amq.services.RabbitMQProducerService;
import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.springframework.context.ApplicationContext;

public class RabbitLogger extends Category {

    private RabbitMQProducerService rabbitMQProducerService;
    private final String moduleName;
    private final String className;

    private String routingKey;

    private RabbitMQProducerService getRabbitMQProducerService(ApplicationContext context) {
        return context.getBean(RabbitMQProducerService.class);
    }

    public RabbitLogger(Class<?> clazz, ApplicationContext context, String routingKey) {
        super(clazz.getSimpleName());
        this.rabbitMQProducerService = getRabbitMQProducerService(context);
        this.moduleName = ModuleName.getModuleName().getName();
        this.className = clazz.getSimpleName();
        this.repository = LogManager.getLoggerRepository();
        this.parent = LogManager.getRootLogger();
        this.routingKey = routingKey;
    }

    private Message buildMessageObj(String message, String level) {
        return new Message(
                new LogEvent(className, message),
                moduleName,
                level
        );
    }

    private Message buildMessageObj(String message, String level, Throwable t) {
        return new Message(
                new LogEvent(className, message, t),
                moduleName,
                level
        );
    }

    @Override
    public void info(Object message) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message,"INFO"), routingKey);
    }

    @Override
    public void info(final Object message, final Throwable t) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message, "INFO", t), routingKey);
    }

    @Override
    public void debug(final Object message) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message,"DEBUG"), routingKey);
    }

    @Override
    public void debug(final Object message, final Throwable t) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message, "DEBUG", t), routingKey);
    }

    @Override
    public void error(final Object message) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message,"ERROR"), routingKey);
    }

    @Override
    public void error(final Object message, final Throwable t) {
        rabbitMQProducerService.sendMessage(buildMessageObj((String) message, "ERROR", t), routingKey);
    }

}
