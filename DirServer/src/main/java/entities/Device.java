package entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {
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

    @Column(name = "hdd_free_space")
    private double hddFreeSpace;

    @Column(name = "online_status")
    private int onlineStatus;

    @ManyToOne
    @JoinColumn(name="group_id")
    private DeviceGroup deviceGroup;
}
