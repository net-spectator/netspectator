package org.net.webcoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import services.ClientListenersStarter;
import utils.ModuleName;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class WebDeviceServiceApplication {
    public static void main(String[] args) {
        ModuleName.getModuleName().setName("WebDeviceService");
        SpringApplication.run(WebDeviceServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(ClientListenersStarter::new);
    }
}
