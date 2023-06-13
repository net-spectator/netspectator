package entities;

import entities.devices.ClientHardwareInfo;
import lombok.*;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "tracked_equipment")
public class TrackedEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Basic
    private int id;
    @Column
    private String UUID;
    @Column
    private String title;
    @Column
    private String ip;
    @Column(name = "online_status")
    private int onlineStatus;
    @Column
    private String macAddress;
    @Column(name = "tracked_status")
    private int blackList;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private DeviceGroup deviceGroup;
    @Transient
    private ClientHardwareInfo deviceInfo;
}
