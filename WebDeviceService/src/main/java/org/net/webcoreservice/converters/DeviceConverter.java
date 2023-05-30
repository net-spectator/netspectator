package org.net.webcoreservice.converters;

import org.net.webcoreservice.dto.DeviceDTO;
import org.net.webcoreservice.entities.Device;

public class DeviceConverter {

    public static DeviceDTO getDeviceDTO(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .uuid(device.getUUID())
                .title(device.getTitle())
                .status(device.getOnlineStatus() == 1)
                .build();
    }
}
