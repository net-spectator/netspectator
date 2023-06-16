package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.Sensors;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.net.webcoreservice.entities.TrackedEquipmentSensors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackedEqSensorsRepository extends JpaRepository<TrackedEquipmentSensors, Long> {

    List<TrackedEquipmentSensors> findByTrackedEquipment(TrackedEquipment trackedEqId);

    Optional<TrackedEquipmentSensors> deleteBySensorsAndTrackedEquipment(Sensors sensorId, TrackedEquipment trackedEqId);

    Optional<TrackedEquipmentSensors> findBySensorsAndTrackedEquipment(Sensors sensorId, TrackedEquipment trackedEqId);
}
