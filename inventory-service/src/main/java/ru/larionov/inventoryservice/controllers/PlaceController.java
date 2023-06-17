package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.PlaceDTO;
import ru.larionov.inventoryservice.services.PlaceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public List<PlaceDTO> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public PlaceDTO getPlaceById(@PathVariable UUID id) {
        return placeService.getPlaceById(id);
    }

    @PostMapping
    public PlaceDTO savePlace(@RequestBody PlaceDTO placeDTO) {
        return placeService.savePlace(placeDTO);
    }
}
