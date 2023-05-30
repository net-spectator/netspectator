package org.net.webcoreservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.net.webcoreservice.converters.DeviceConverter;
import org.net.webcoreservice.dto.DeviceDTO;
import org.net.webcoreservice.entities.Device;
import org.net.webcoreservice.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public List<DeviceDTO> getAllDevices(){
        List<DeviceDTO> list = deviceRepository.findAll()
                .stream()
                .map(DeviceConverter::getDeviceDTO)
                .collect(Collectors.toList());
         return list;
    }

}
