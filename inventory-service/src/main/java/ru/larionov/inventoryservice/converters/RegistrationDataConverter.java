package ru.larionov.inventoryservice.converters;

import ru.larionov.inventoryservice.dto.RegistrationDataDTO;
import ru.larionov.inventoryservice.entity.RegistrationEvent;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.entity.RegistrationStatus;

public class RegistrationDataConverter {

    public static RegistrationDataDTO toDTO(RegistrationStatus registrationStatus) {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();

        registrationDataDTO.setRegistrationNumber(registrationStatus.getRegistrationNumber());
        registrationDataDTO.setPlaceNumber(registrationStatus.getPlaceNumber());
        registrationDataDTO.setEventsType(registrationStatus.getEventsType());
        registrationDataDTO.setDate(registrationStatus.getDate());

        return registrationDataDTO;
    }

    public static RegistrationDataDTO toDTO(RegistrationEvent registrationEvent) {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();

        registrationDataDTO.setRegistrationNumber(registrationEvent.getRegistrationNumber());
        registrationDataDTO.setPlaceNumber(registrationEvent.getPlaceNumber());
        registrationDataDTO.setEventsType(registrationEvent.getEventsType());
        registrationDataDTO.setDate(registrationEvent.getDate());

        return registrationDataDTO;
    }

    public static RegistrationEvent toEvent(RegistrationStatus registrationStatus) {
        RegistrationEvent registrationEvent = new RegistrationEvent();

        registrationEvent.setRegistrationNumber(registrationStatus.getRegistrationNumber());
        registrationEvent.setPlaceNumber(registrationStatus.getPlaceNumber());
        registrationEvent.setEventsType(registrationStatus.getEventsType());
        registrationEvent.setDate(registrationStatus.getDate());

        return registrationEvent;
    }
}
