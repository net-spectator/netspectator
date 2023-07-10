package org.net.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class LoggerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerServiceApplication.class);
    }
}
