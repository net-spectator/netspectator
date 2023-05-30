package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.DeviceDTO;
import org.net.webcoreservice.services.DeviceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/v1")
@CrossOrigin("*")
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("/getAll")
    public List<DeviceDTO> getAllDevices(){
        return deviceService.getAllDevices();
    }
}
