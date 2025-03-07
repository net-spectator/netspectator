package entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name = "sensors")
public class Sensors {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String sensorTitle;

    @OneToMany(mappedBy = "sensors", cascade = CascadeType.ALL)
    private List<TrackedEquipmentSensors> trackedEquipmentSensorsList;
}
