package org.net.webcoreservice.service;

import org.net.webcoreservice.entities.Sensors;
import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.TrackedEqSensorsDto;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.net.webcoreservice.entities.TrackedEquipmentSensors;
import org.net.webcoreservice.repository.SensorsRepository;
import org.net.webcoreservice.repository.TrackedEqSensorsRepository;
import org.net.webcoreservice.repository.TrackedEquipmentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackedEqSensorsService {

    private final TrackedEqSensorsRepository trackedEqSensorsRepository;
    private final SensorsRepository sensorsRepository;
    private final TrackedEquipmentRepository trackedEquipmentRepository;

    public List<TrackedEquipmentSensors> findAll() {
        return trackedEqSensorsRepository.findAll();
    }

    public List<TrackedEquipmentSensors> findByEquipmentId(Long id) {
        TrackedEquipment tr = new TrackedEquipment();
        tr.setId(id);
        return trackedEqSensorsRepository.findByTrackedEquipment(tr);
    }

    @Transactional
    public Optional<TrackedEquipmentSensors> deleteSensorFromEquipment(Long sensorId, Long equipmentId) {
        TrackedEquipment tr = new TrackedEquipment();
        tr.setId(equipmentId);
        Sensors s = new Sensors();
        s.setId(sensorId);
        return trackedEqSensorsRepository.deleteBySensorsAndTrackedEquipment(s, tr);
    }

    public void addSensorInEquipment(TrackedEqSensorsDto dto) {
        TrackedEquipmentSensors equipmentSensors = new TrackedEquipmentSensors();
        equipmentSensors.setSensors(sensorsRepository.getById(dto.getSensorId()));
        equipmentSensors.setTrackedEquipment(trackedEquipmentRepository.getById(dto.getEquipmentId()));
        trackedEqSensorsRepository.save(equipmentSensors);
    }

    public boolean sensorOrVapExists(Long sensorId, Long equipId) {
        if (sensorsRepository.existsById(sensorId) && trackedEquipmentRepository.existsById(equipId)) {
            return true;
        }
        return false;
    }

    public boolean isExists(Long sensorId, Long equipId) {
        Sensors sensors = new Sensors();
        sensors.setId(sensorId);
        TrackedEquipment tr = new TrackedEquipment();
        tr.setId(equipId);
        return trackedEqSensorsRepository.findBySensorsAndTrackedEquipment(sensors, tr).isPresent();
    }
}