package ru.larionov.inventoryservice.converters;

import inventory.dtos.DeviceDTO;
import ru.larionov.inventoryservice.entity.Device;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;

public class DeviceConverter {

    public static DeviceDTO toDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId(device.getId());
        deviceDTO.setName(device.getName());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setResponsible(device.getResponsible());
        deviceDTO.setState(device.getState());
        deviceDTO.setRegistrationNumber(device.getRegistrationNumber().getId());
        if (device.getTypeMaterial() != null)
            deviceDTO.setTypeMaterial(TypeMaterialConverter.toDTO(device.getTypeMaterial()));
        if (device.getVendor() != null)
            deviceDTO.setVendor(VendorConverter.toDTO(device.getVendor()));
        deviceDTO.setUser(device.getUser());

        return deviceDTO;
    }

    public static DeviceDTO toDTO(DeviceWithDetailsDTO device) {
        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId(device.getId());
        deviceDTO.setName(device.getName());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setResponsible(device.getResponsibleId());
        deviceDTO.setState(device.getState());
        if (device.getRegistrationNumber() != null)
            deviceDTO.setRegistrationNumber(device.getRegistrationNumber());
        if (device.getTypeMaterial() != null)
            deviceDTO.setTypeMaterial(TypeMaterialConverter.toDTO(device.getTypeMaterial()));
        if (device.getVendor() != null)
            deviceDTO.setVendor(VendorConverter.toDTO(device.getVendor()));
        deviceDTO.setUser(device.getUserId());

        return deviceDTO;
    }

    public static Device fromDTO(DeviceDTO deviceDTO) {
        Device device = new Device();

        device.setId(deviceDTO.getId());
        device.setName(deviceDTO.getName());
        device.setDescription(deviceDTO.getDescription());
        device.setResponsible(deviceDTO.getResponsible());
        device.setState(deviceDTO.getState());
        device.setRegistrationNumber(new RegistrationNumber(deviceDTO.getRegistrationNumber()));
        if (deviceDTO.getTypeMaterial() != null)
            device.setTypeMaterial(TypeMaterialConverter.fromDTO(deviceDTO.getTypeMaterial()));
        if (deviceDTO.getVendor() != null)
            device.setVendor(VendorConverter.fromDTO(deviceDTO.getVendor()));
        device.setUser(deviceDTO.getUser());

        return device;
    }
}
