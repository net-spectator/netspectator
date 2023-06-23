package org.net.webcoreservice.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tracked_equipment")
public class TrackedEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String equipmentUuid;

    @Column(name = "title")
    private String equipmentTitle;

    @Column(name = "ip")
    private String equipmentIpAddress;

    @Column(name = "online_status")
    private int equipmentOnlineStatus;

    @Column(name = "mac_address")
    private String equipmentMacAddress;

    @Column(name = "black_list", columnDefinition = "integer default 0")
    private int blackList;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "type")
    private EquipmentType typeId;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "trackedEquipment", cascade = CascadeType.ALL)
    private List<TrackedEquipmentSensors> trackedEquipmentSensorsList;

}
