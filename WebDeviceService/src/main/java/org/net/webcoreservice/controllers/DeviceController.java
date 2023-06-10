package org.net.webcoreservice.controllers;

import entities.Connection;
import entities.Device;
import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.DeviceDTO;
import org.net.webcoreservice.services.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.ClientListenersDataBus;

import java.net.SocketAddress;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/v1")
@CrossOrigin("*")
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<SocketAddress> getAllDevices() {
        return ClientListenersDataBus.getDisabledClientsList();
    }
}
