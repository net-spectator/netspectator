package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import inventory.dtos.DeviceDTO;
import ru.larionov.inventoryservice.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public List<DeviceDTO> getAllDevices(@RequestHeader("x-introspect") UUID userID,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                                         @RequestParam(name = "size", defaultValue = "30") Integer size,
                                         @RequestParam(name = "place_id", required = false) UUID place,
                                         @RequestParam(name = "hierarchy", defaultValue = "false") Boolean hierarchy,
                                         @RequestParam(name = "reg_number", required = false) Long number,
                                         @RequestParam(name = "name_part", required = false) String namePart,
                                         @RequestParam(name = "vendor_id", required = false) UUID vendor) {
        return deviceService.getAllDevices(userID, page, size, place, hierarchy, number, namePart, vendor);
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
