package ru.larionov.inventoryservice.converters;

import inventory.dtos.PlaceDTO;
import ru.larionov.inventoryservice.entity.Place;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.repository.PlaceRepository;

public class PlaceConverter {

    public static PlaceDTO toDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();

        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setDescription(place.getDescription());
        placeDTO.setParent_id(place.getParent().getId());
        placeDTO.setRegistrationNumber(place.getRegistrationNumber().getId());

        return placeDTO;
    }

    public static Place fromDTO(PlaceDTO placeDTO, PlaceRepository placeRepository) {
        Place place = new Place();

        place.setId(placeDTO.getId());
        place.setName(placeDTO.getName());
        place.setDescription(placeDTO.getDescription());
        place.setParent(
                placeRepository.getReferenceById(placeDTO.getParent_id())
        );
        place.setRegistrationNumber(new RegistrationNumber(placeDTO.getRegistrationNumber()));

        return place;
    }
}
