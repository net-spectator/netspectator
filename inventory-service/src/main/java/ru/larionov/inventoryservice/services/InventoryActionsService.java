package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larionov.inventoryservice.converters.RegistrationDataConverter;
import inventory.dtos.RegistrationDataDTO;
import ru.larionov.inventoryservice.entity.RegistrationEvent;
import inventory.enums.RegistrationEventsType;
import ru.larionov.inventoryservice.entity.RegistrationStatus;
import inventory.enums.TypePlace;
import ru.larionov.inventoryservice.repository.PlaceDetailsRepository;
import ru.larionov.inventoryservice.repository.RegistrationEventsRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;
import ru.larionov.inventoryservice.repository.RegistrationStatusesRepository;
import ru.larionov.inventoryservice.views.PlaceDetailsDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryActionsService {

    private final RegistrationNumberRepository registrationNumberRepository;
    private final RegistrationStatusesRepository registrationStatusesRepository;
    private final RegistrationEventsRepository registrationEventsRepository;
    private final PlaceDetailsRepository placeDetailsRepository;
    private final NotificationService notificationService;


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

        PlaceDetailsDTO object = placeDetailsRepository.findByPlaceId(currentStatusObject.getRegistrationNumber());
        PlaceDetailsDTO oldPlace = placeDetailsRepository.findByPlaceId(currentStatusObject.getPlaceNumber());

        RegistrationEvent kreditEvent = RegistrationDataConverter.toEvent(currentStatusObject);
        kreditEvent.setEventsType(RegistrationEventsType.CREDIT);
        kreditEvent.setDate(curDate);

        registrationEventsRepository.save(kreditEvent);

        notificationService.sendNotification(7L,
                String.format("%s [%s] списано %s [%s]",
                        object.getTypePlace().equals(TypePlace.DEVICE) ? "Устройство" : "Материал",
                        object.getPlaceId(),
                        oldPlace.getTypePlace().equals(TypePlace.DEVICE) ? "из устройства" : "из " + oldPlace.getName(),
                        oldPlace.getPlaceId()));

        currentStatusObject.setPlaceNumber(placeId);
        currentStatusObject.setEventsType(RegistrationEventsType.DEBET);
        currentStatusObject.setDate(curDate);

        PlaceDetailsDTO newPlace = placeDetailsRepository.findByPlaceId(currentStatusObject.getPlaceNumber());

        RegistrationEvent debetEvent = RegistrationDataConverter.toEvent(currentStatusObject);

        registrationEventsRepository.save(debetEvent);

        notificationService.sendNotification(5L,
                String.format("%s [%s] %s %s %s [%s]",
                        object.getTypePlace().equals(TypePlace.DEVICE) ? "Устройство" : "Материал",
                        object.getPlaceId(),
                        object.getTypePlace().equals(TypePlace.DEVICE) ? "перемещен в" : "перемещено в",
                        newPlace.getTypePlace().equals(TypePlace.DEVICE) ? "устройство" : "место",
                        newPlace.getName(),
                        oldPlace.getPlaceId()));
        if (oldPlace.getTypePlace().equals(TypePlace.DEVICE)){
            notificationService.sendNotification(8L,
                    String.format("Изменен состав устовйства %s [%s]",
                            oldPlace.getName(),
                            oldPlace.getPlaceId()));
        }
        if (newPlace.getTypePlace().equals(TypePlace.DEVICE)){
            notificationService.sendNotification(8L,
                    String.format("Изменен состав устовйства %s [%s]",
                            newPlace.getName(),
                            newPlace.getPlaceId()));
        }
    }
}