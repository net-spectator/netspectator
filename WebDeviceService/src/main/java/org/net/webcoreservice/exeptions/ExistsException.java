package org.net.webcoreservice.exeptions;

public class ExistsException extends RuntimeException{

    public ExistsException(String message) {
        super(message);
    }
}
