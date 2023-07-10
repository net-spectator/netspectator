package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.larionov.inventoryservice.converters.PlaceConverter;
import inventory.dtos.PlaceDTO;
import ru.larionov.inventoryservice.entity.Place;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.repository.PlaceRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RegistrationNumberRepository registrationNumberRepository;

    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findAll(
                Sort.by("name"))
                .stream()
                .map(PlaceConverter::toDTO)
                .collect(Collectors.toList());
    }

    public PlaceDTO getPlaceById(UUID id) {
        return PlaceConverter.toDTO(
                placeRepository.getReferenceById(id)
        );
    }

    public PlaceDTO savePlace(PlaceDTO placeDTO) {
        Place place = PlaceConverter.fromDTO(placeDTO, placeRepository);

        if (place.getId() == null) {
            RegistrationNumber registrationNumber = new RegistrationNumber();
            place.setRegistrationNumber(
                    registrationNumberRepository.save(registrationNumber)
            );
        }

        return PlaceConverter.toDTO(placeRepository.save(place));
    }

}
