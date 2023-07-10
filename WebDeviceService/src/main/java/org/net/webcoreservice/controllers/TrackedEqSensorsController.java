package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.TrackedEqSensorsConverter;
import org.net.webcoreservice.dto.TrackedEqSensorsDto;
import org.net.webcoreservice.service.TrackedEqSensorsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.ClientListenersDataBus;

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSensorFromEquipment(@RequestParam Long sensorId, Long equipId) {
        trackedEqSensorsService.deleteSensorFromEquipment(sensorId, equipId);
    }

    @GetMapping("/{id}")
    public List<TrackedEqSensorsDto> getByEquipId(@PathVariable Long id) {
        return trackedEqSensorsService.findByEquipmentId(id).stream().map(trackedEqSensorsConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSensorInEquip(@RequestBody TrackedEqSensorsDto dto) {
        trackedEqSensorsService.addSensorInEquipment(dto);
        disconnect(dto.getEquipmentId());
    }

    private void disconnect(Long equipmentId) {
        ClientListenersDataBus.disconnectClient(equipmentId);
    }
}
