package org.net.webcoreservice.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tracked_equipment_sensors")
public class TrackedEquipmentSensors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tracked_equipment_id")
    private TrackedEquipment trackedEquipment;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensors sensors;

}
