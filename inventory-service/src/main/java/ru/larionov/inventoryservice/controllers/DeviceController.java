package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import ru.larionov.inventoryservice.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public List<DeviceDTO> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public DeviceDTO getDeviceById(@PathVariable UUID id) {
        return deviceService.getDeviceById(id);
    }

    @PostMapping
    public DeviceDTO saveDevice(@RequestBody DeviceDTO deviceDTO) {
        return deviceService.saveDevice(deviceDTO);
    }
}
