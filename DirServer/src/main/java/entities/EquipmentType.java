package entities;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "equipment_type")
public class EquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long typeId;

    @Column(name = "title")
    @NotNull
    private String typeTitle;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "typeId")
    private List<TrackedEquipment> trackedEquipmentList;
}
