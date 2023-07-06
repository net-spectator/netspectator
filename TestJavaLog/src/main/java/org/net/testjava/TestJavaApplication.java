package org.net.testjava;

import org.springframework.context.ApplicationContext;
import utils.ModuleName;
import utils.NSLogger;
import utils.SpringContext;

public class TestJavaApplication {

    public static void main(String[] args) {
        String routingKey = "nsLogs2311";
        ModuleName.getModuleName().setName("TestJAVA");
        ApplicationContext context = SpringContext.initializeContext(args);
        NSLogger logger = new NSLogger(TestJavaApplication.class, context, routingKey);
        logger.setLocalLog(false);
        logger.setServerLog(true);
        logger.info("info message");
        logger.debug("debug message");
        logger.error("error message");
        SpringContext.closeContext(context);
    }
}
