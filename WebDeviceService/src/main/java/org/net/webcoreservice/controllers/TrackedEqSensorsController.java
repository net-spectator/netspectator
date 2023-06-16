package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.TrackedEqSensorsConverter;
import org.net.webcoreservice.dto.TrackedEqSensorsDto;
import org.net.webcoreservice.exeptions.AppError;
import org.net.webcoreservice.service.TrackedEqSensorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eqsensors")
public class TrackedEqSensorsController {

    private final TrackedEqSensorsService trackedEqSensorsService;
    private final TrackedEqSensorsConverter trackedEqSensorsConverter;

    @GetMapping
    public List<TrackedEqSensorsDto> getAll() {
        return trackedEqSensorsService.findAll().stream().map(trackedEqSensorsConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    public void deleteSensorFromEquipment(@RequestParam Long sensorId, @RequestParam Long equipId) {
        trackedEqSensorsService.deleteSensorFromEquipment(sensorId, equipId);
    }

    @GetMapping("/{id}")
    public List<TrackedEqSensorsDto> getByEquipId(@PathVariable Long id) {
        return trackedEqSensorsService.findByEquipmentId(id).stream().map(trackedEqSensorsConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> addSensorInEquip(@RequestBody TrackedEqSensorsDto dto) {
        if (dto.getSensorId() < 0 || dto.getEquipmentId() < 0) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "id сенсора или оборудования не могут быть отрицательными"), HttpStatus.BAD_REQUEST);
        }
        if (!trackedEqSensorsService.sensorOrVapExists(dto.getSensorId(), dto.getEquipmentId())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Такого сенсора или оборудования не существует"), HttpStatus.BAD_REQUEST);
        }
        if (trackedEqSensorsService.isExists(dto.getSensorId(), dto.getEquipmentId())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Связь уже существует"), HttpStatus.BAD_REQUEST);
        }
        trackedEqSensorsService.addSensorInEquipment(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
