package utils;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.LinkedList;
import java.util.List;

public class NSLogger extends Category {

    private final Logger localLogger;
    private RabbitLogger serverLogger;
    private final List<Category> loggerFactory;
    private final Class<?> clazz;
    private final String moduleName;

    public boolean isLocalLog() {
        return loggerFactory.contains(localLogger);
    }

    public void setLocalLog(boolean localLog) {
        if (localLog) {
            loggerFactory.add(localLogger);
        } else {
            loggerFactory.remove(localLogger);
        }
    }

    public boolean isServerLog() {
        return loggerFactory.contains(serverLogger);
    }

    public void setServerLog(boolean serverLog) {
        if (serverLog) {
            loggerFactory.add(serverLogger);
        } else {
            loggerFactory.remove(serverLogger);
        }
    }

    public NSLogger(Class<?> clazz, ApplicationContext context, String routingKey) {
        super(clazz.getSimpleName());
        loggerFactory = new LinkedList<>();
        moduleName = ModuleName.getModuleName().getName();
        this.clazz = clazz;
        this.serverLogger = new RabbitLogger(clazz, context, routingKey);
        localLogger = Logger.getLogger(clazz.getName());
        init();
    }

    public NSLogger(Class<?> clazz) {
        super(clazz.getSimpleName());
        loggerFactory = new LinkedList<>();
        moduleName = ModuleName.getModuleName().getName();
        this.clazz = clazz;
        localLogger = Logger.getLogger(clazz.getName());
        init();
    }

    private void init() {
        setLocalLog(true);
        setServerLog(false);
    }

    public void info(String message) {
        loggerFactory.forEach(category -> category.info(message));
    }

    public void info(String message, Throwable t) {
        loggerFactory.forEach(category -> category.info(message, t));
    }

    public void debug(String message) {
        loggerFactory.forEach(category -> category.debug(message));
    }

    public void debug(String message, Throwable t) {
        loggerFactory.forEach(category -> category.debug(message, t));
    }

    public void error(String message) {
        loggerFactory.forEach(category -> category.error(message));
    }

    public void error(String message, Throwable t) {
        loggerFactory.forEach(category -> category.error(message, t));
    }
}
