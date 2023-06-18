package ru.larionov.inventoryservice.converters;

import ru.larionov.inventoryservice.dto.DeviceDTO;
import ru.larionov.inventoryservice.entity.Device;

public class DeviceConverter {

    public static DeviceDTO toDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId(device.getId());
        deviceDTO.setName(device.getName());
        deviceDTO.setDescription(device.getName());
        deviceDTO.setResponsible(device.getResponsible());
        deviceDTO.setState(device.getState());
        deviceDTO.setRegistrationNumber(device.getRegistrationNumber());
        deviceDTO.setTypeMaterial(device.getTypeMaterial());
        deviceDTO.setVendor(device.getVendor());
        deviceDTO.setUser(device.getUser());

        return deviceDTO;
    }

    public static Device fromDTO(DeviceDTO deviceDTO) {
        Device device = new Device();

        device.setId(deviceDTO.getId());
        device.setName(deviceDTO.getName());
        device.setDescription(deviceDTO.getName());
        device.setResponsible(deviceDTO.getResponsible());
        device.setState(deviceDTO.getState());
        device.setRegistrationNumber(deviceDTO.getRegistrationNumber());
        device.setTypeMaterial(deviceDTO.getTypeMaterial());
        device.setVendor(deviceDTO.getVendor());
        device.setUser(deviceDTO.getUser());

        return device;
    }
}
