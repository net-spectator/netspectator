package org.net.webcoreservice.converters;

import org.net.webcoreservice.dto.TrackedEquipmentDto;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.springframework.stereotype.Component;

@Component
public class TrackedEquipmentConverter {

    public TrackedEquipmentDto entityToDto(TrackedEquipment trackedEquipment) {
        TrackedEquipmentDto dto = new TrackedEquipmentDto();
        dto.setId(trackedEquipment.getId());
        dto.setTitle(trackedEquipment.getEquipmentTitle());
        dto.setMac(trackedEquipment.getEquipmentMacAddress());
        dto.setUuid(trackedEquipment.getEquipmentUuid());
        dto.setOnlineStatus(trackedEquipment.getEquipmentOnlineStatus());
        dto.setIp(trackedEquipment.getEquipmentIpAddress());
        return dto;
    }
}
