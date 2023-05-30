package org.net.webcoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class WebDeviceServiceApplication {

    public static void main(String[] args) {
        tcpServerStart();
        SpringApplication.run(WebDeviceServiceApplication.class, args);
    }

    /*
     стартует TCP сервер для H2 (при использовании БД отключить),
     необходим для совместного использования базы данных между DirServer и Web
    */
    private static void tcpServerStart(){
        try {
            org.h2.tools.Server server = org.h2.tools.Server.createTcpServer().start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
