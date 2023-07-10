package ru.larionov.inventoryservice.exeptions;

public class VendorNotFound extends RuntimeException{
    public VendorNotFound(String message) {
        super(String.format("Vendor with id: %s not found", message));
    }
}
