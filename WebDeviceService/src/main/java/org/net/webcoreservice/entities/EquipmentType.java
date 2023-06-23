package org.net.webcoreservice.entities;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "equipmentType")
public class EquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long typeId;

    @Column(name = "title")
    @NotNull
    private String typeTitle;

    @OneToMany(mappedBy = "typeId")
    private List<TrackedEquipment> trackedEquipmentList;
}
