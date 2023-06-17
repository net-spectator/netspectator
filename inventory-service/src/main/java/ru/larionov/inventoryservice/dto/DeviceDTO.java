package ru.larionov.inventoryservice.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.entity.StateEquipment;
import ru.larionov.inventoryservice.entity.TypeMaterial;
import ru.larionov.inventoryservice.entity.Vendor;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class DeviceDTO {
    private UUID id;
    private String name;
    private String description;
    private StateEquipment state;
    private UUID responsible;
    private UUID user;
    private TypeMaterial typeMaterial;
    private Vendor vendor;
    private RegistrationNumber registrationNumber;
}
