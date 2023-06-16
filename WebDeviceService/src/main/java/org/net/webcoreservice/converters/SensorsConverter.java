package org.net.webcoreservice.converters;

import org.net.webcoreservice.dto.SensorDto;
import org.net.webcoreservice.entities.Sensors;
import org.springframework.stereotype.Component;

@Component
public class SensorsConverter {

    public SensorDto entityToDto(Sensors sensors) {
        SensorDto dto = new SensorDto();
        dto.setId(sensors.getId());
        dto.setTitle(sensors.getSensorTitle());
        return dto;
    }
}
