package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.larionov.inventoryservice.views.PlaceDetailsDTO;

public interface PlaceDetailsRepository extends JpaRepository<PlaceDetailsDTO, Long> {

    PlaceDetailsDTO findByPlaceId(Long placeId);
}
