package ru.larionov.inventoryservice.exeptions;

public class TypeMaterialNotFound extends RuntimeException{
    public TypeMaterialNotFound(String message) {
        super(String.format("Type material with id: %s not found", message));
    }
}
