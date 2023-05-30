package org.net.webcoreservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String UUID;

    @Column
    private String title;

    @Column
    private String ip;

    @Column
    private double hddFreeSpace;

    @Column(name = "online_status")
    private int onlineStatus;

    @ManyToOne
    @JoinColumn(name="group_id")
    private DeviceGroup deviceGroup;
}
