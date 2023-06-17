package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larionov.inventoryservice.converters.DeviceConverter;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import ru.larionov.inventoryservice.entity.Device;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.repository.DeviceRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final RegistrationNumberRepository registrationNumberRepository;

    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll(
                Sort.by("name"))
                .stream()
                .map(DeviceConverter::toDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(UUID id) {
        return DeviceConverter.toDTO(
                deviceRepository.getReferenceById(id)
        );
    }

    @Transactional
    public DeviceDTO saveDevice(DeviceDTO deviceDTO) {
        Device device = DeviceConverter.fromDTO(deviceDTO);

        if (device.getId() == null){
            RegistrationNumber registrationNumber = new RegistrationNumber();
            device.setRegistrationNumber(registrationNumberRepository.save(registrationNumber));
        }

        return DeviceConverter.toDTO(deviceRepository.save(device));
    }
}
