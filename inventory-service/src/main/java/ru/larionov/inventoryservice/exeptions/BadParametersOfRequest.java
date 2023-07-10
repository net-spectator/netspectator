package ru.larionov.inventoryservice.exeptions;

public class BadParametersOfRequest extends RuntimeException {

    public BadParametersOfRequest(String message) {
        super("Bad parameter of request because: " + message);
    }
}
