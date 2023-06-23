package org.net.webcoreservice.converters;

import org.net.webcoreservice.dto.TrackedEqSensorsDto;
import org.net.webcoreservice.entities.TrackedEquipmentSensors;
import org.springframework.stereotype.Component;

@Component
public class TrackedEqSensorsConverter {

    public TrackedEqSensorsDto entityToDto(TrackedEquipmentSensors tes) {
        TrackedEqSensorsDto trackedEqSensorsDto = new TrackedEqSensorsDto();
        trackedEqSensorsDto.setSensorId(tes.getSensors().getId());
        trackedEqSensorsDto.setEquipmentId(tes.getTrackedEquipment().getId());
        return trackedEqSensorsDto;
    }
}
