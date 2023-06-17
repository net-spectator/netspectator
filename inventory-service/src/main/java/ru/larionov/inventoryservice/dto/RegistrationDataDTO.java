package ru.larionov.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.larionov.inventoryservice.entity.RegistrationEventsType;
import ru.larionov.inventoryservice.entity.RegistrationNumber;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationDataDTO {

    private UUID id;
    private Long registrationNumber;
    private Long placeNumber;
    private RegistrationEventsType eventsType;
    private Date date;

}
