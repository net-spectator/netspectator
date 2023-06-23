package ru.larionov.inventoryservice.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class VendorDTO {

    private UUID id;
    private String name;
}
