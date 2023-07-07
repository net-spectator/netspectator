package org.net.webcoreservice.service;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.SensorDto;
import org.net.webcoreservice.entities.Sensors;
import org.net.webcoreservice.repository.SensorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorsService {

    private final SensorsRepository sensorsRepository;

    public Optional<Sensors> findById(Long id) {
        return sensorsRepository.findById(id);
    }

    public List<Sensors> findAll() {
        return sensorsRepository.findAll();
    }

    public void deleteById(Long id) {
        sensorsRepository.deleteById(id);
    }

    public Sensors createNewSensor(String title) {
        Sensors sensors = new Sensors();
        sensors.setSensorTitle(title);
        return sensorsRepository.save(sensors);
    }
}
