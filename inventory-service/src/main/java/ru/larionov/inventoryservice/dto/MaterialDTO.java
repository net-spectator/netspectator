package ru.larionov.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.larionov.inventoryservice.entity.*;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class MaterialDTO {
    private UUID id;
    private String name;
    private String description;
    private String serialNumber;
    private StateEquipment state;
    // TODO: replace type of the field "responsible" on userDTO
    private UUID responsible;
    private TypeMaterial typeMaterial;
    private Vendor vendor;
    private RegistrationNumber registrationNumber;

}
