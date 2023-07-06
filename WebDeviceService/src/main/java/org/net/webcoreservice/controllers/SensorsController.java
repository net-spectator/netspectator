package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.SensorsConverter;
import org.net.webcoreservice.dto.SensorDto;
import org.net.webcoreservice.entities.Sensors;
import org.net.webcoreservice.exeptions.ResourceNotFoundException;
import org.net.webcoreservice.service.SensorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final SensorsConverter sensorsConverter;

    @GetMapping
    public List<SensorDto> getAllSensors() {
        return sensorsService.findAll().stream().map(sensorsConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDto getSensorById(@PathVariable Long id) {
        return sensorsConverter.entityToDto(sensorsService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Сенсор с id:" + id + " не найден")));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<SensorDto> createNewSensor(@RequestBody SensorDto sensorDto) {
        Sensors sensor = sensorsService.createNewSensor(sensorDto);
        SensorDto createdSensorDto = sensorsConverter.entityToDto(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensorById(@PathVariable Long id) {
        sensorsService.deleteById(id);
    }
}
