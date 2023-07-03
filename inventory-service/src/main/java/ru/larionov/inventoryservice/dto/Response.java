package ru.larionov.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@NoArgsConstructor
@Service
@Getter
public class Response {
    private LocalDateTime date;
    private String message;

    private Boolean error;

    public Response(LocalDateTime date, String message) {
        this.date = date;
        this.message = message;
        this.error = false;
    }

    public Response(LocalDateTime date, String message, Boolean error) {
        this.date = date;
        this.message = message;
        this.error = error;
    }
}
