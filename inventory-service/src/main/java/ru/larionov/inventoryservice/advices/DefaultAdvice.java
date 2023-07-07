package ru.larionov.inventoryservice.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.larionov.inventoryservice.dto.Response;
import ru.larionov.inventoryservice.exeptions.BadParametersOfRequest;
import ru.larionov.inventoryservice.exeptions.TypeMaterialNotFound;
import ru.larionov.inventoryservice.exeptions.VendorNotFound;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        return new ResponseEntity<>(
                new Response(
                        LocalDateTime.now(),
                        e.getMessage(),
                        true),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadParametersOfRequest.class, TypeMaterialNotFound.class, VendorNotFound.class})
    public ResponseEntity<Response> handleVadRequest(RuntimeException e) {
        return new ResponseEntity<>(
                new Response(
                        LocalDateTime.now(),
                        e.getMessage(),
                        true
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
