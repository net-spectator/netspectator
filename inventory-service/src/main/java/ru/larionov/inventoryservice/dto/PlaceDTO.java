package ru.larionov.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class PlaceDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID parent_id;
    private RegistrationNumber registrationNumber;
}
