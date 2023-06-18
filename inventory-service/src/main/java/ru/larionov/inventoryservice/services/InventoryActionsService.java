package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larionov.inventoryservice.converters.RegistrationDataConverter;
import ru.larionov.inventoryservice.dto.RegistrationDataDTO;
import ru.larionov.inventoryservice.entity.RegistrationEvent;
import ru.larionov.inventoryservice.entity.RegistrationEventsType;
import ru.larionov.inventoryservice.entity.RegistrationStatus;
import ru.larionov.inventoryservice.repository.RegistrationEventsRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;
import ru.larionov.inventoryservice.repository.RegistrationStatusesRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryActionsService {

    private final RegistrationNumberRepository registrationNumberRepository;
    private final RegistrationStatusesRepository registrationStatusesRepository;
    private final RegistrationEventsRepository registrationEventsRepository;


    public RegistrationDataDTO getRegistrationDataById(Long id) {
        return RegistrationDataConverter.toDTO(registrationStatusesRepository.getReferenceById(id));
    }

    public List<RegistrationDataDTO> getHistoryInventoryActions(Long id) {
        return registrationEventsRepository.findAllByRegistrationNumber(id)
                .stream()
                .map(RegistrationDataConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addIntoPlace(Long objectId, Long placeId) {
        RegistrationStatus currentStatusObject = registrationStatusesRepository.getReferenceById(objectId);
        Date curDate = new Date();

        RegistrationEvent kreditEvent = RegistrationDataConverter.toEvent(currentStatusObject);
        kreditEvent.setEventsType(RegistrationEventsType.CREDIT);
        kreditEvent.setDate(curDate);

        registrationEventsRepository.save(kreditEvent);

        currentStatusObject.setPlaceNumber(placeId);
        currentStatusObject.setEventsType(RegistrationEventsType.DEBET);
        currentStatusObject.setDate(curDate);

        RegistrationEvent debetEvent = RegistrationDataConverter.toEvent(currentStatusObject);

        registrationEventsRepository.save(debetEvent);
    }
}