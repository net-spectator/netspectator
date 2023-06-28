package utils;

import org.apache.log4j.Logger;

public class NSLogger {
    private Logger logger;
    private boolean disabled; //позволяет отключать логирование событий (можно будет потом убрать если не нужно)
    private final Class<?> clazz;

    private final String moduleName;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public NSLogger(Class<?> clazz) {
        moduleName = clazz.getModule().getName();
        this.clazz = clazz;
        init();
    }

    private void init() {
        disabled = false;
        logger = Logger.getLogger(clazz.getName());
    }

    public void info(String message) {
        if (!disabled) {
            logger.info(message);
        }
    }

    public void info(String message, Throwable t) {
        if (!disabled) {
            logger.info(message, t);
        }
    }

    public void error(String message) {
        if (!disabled) {
            logger.error(message);
        }
    }

    public void error(String message, Throwable t) {
        if (!disabled) {
            logger.error(message, t);
        }
    }

}
