package utils;

import org.apache.log4j.Logger;

public class NSLogger {
    private Logger logger;

    private final Class<?> clazz;

    public NSLogger(Class<?> clazz) {
        this.clazz = clazz;
        init();
    }

    private void init() {
        logger = Logger.getLogger(clazz.getName());
    }

    public void info(String message) {
        logger.info(message);
    }

    public void info(String message, Throwable t) {
        logger.info(message, t);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

}
