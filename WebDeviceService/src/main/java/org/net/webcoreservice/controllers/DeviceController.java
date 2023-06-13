package org.net.webcoreservice.controllers;

import entities.devices.ClientHardwareInfo;
import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.services.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.ClientListenersDataBus;

@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/v1")
@CrossOrigin("*")
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public ClientHardwareInfo getAllDevices() {
        return ClientListenersDataBus.getConnection(0).getDevice().getDeviceInfo();
    }
}
