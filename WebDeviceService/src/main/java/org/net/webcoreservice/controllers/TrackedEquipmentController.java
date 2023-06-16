package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.TrackedEquipmentConverter;
import org.net.webcoreservice.dto.TrackedEquipmentDto;
import org.net.webcoreservice.exeptions.ResourceNotFoundException;
import org.net.webcoreservice.service.TrackedEquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment")
public class TrackedEquipmentController {

    private final TrackedEquipmentService trackedEquipmentService;
    private final TrackedEquipmentConverter converter;

    @GetMapping
    public List<TrackedEquipmentDto> getAllEquipment() {
        return trackedEquipmentService.findAll().stream().map(converter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TrackedEquipmentDto getEquipmentById(@PathVariable Long id) {
        return converter.entityToDto(trackedEquipmentService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Оборудование с id:" + id + " не найдено")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewEquipment(@RequestBody TrackedEquipmentDto trackedEquipmentDto) {
        trackedEquipmentService.createNewTrackedEquipment(trackedEquipmentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trackedEquipmentService.deleteById(id);
    }
}
