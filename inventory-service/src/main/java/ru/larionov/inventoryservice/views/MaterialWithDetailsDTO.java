package ru.larionov.inventoryservice.views;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
import inventory.enums.StateEquipment;
import ru.larionov.inventoryservice.entity.TypeMaterial;
import ru.larionov.inventoryservice.entity.Vendor;

import java.util.UUID;

@Entity
@Immutable
@Subselect("SELECT\n" +
        "    m.id, p.name, p.description, serial_number, state, responsible_id, type_id, vendor_id, registration_id,\n" +
        "    tm.name,\n" +
        "    v.name,\n" +
        "    p.id AS place_id, \n" +
        "    p.name AS place_name," +
        "    p.parent_id, \n" +
        "    p.registration_id AS place_registration_id\n" +
        "FROM materials m\n" +
        "    LEFT JOIN type_materials tm on m.type_id = tm.id\n" +
        "    LEFT JOIN vendors v on m.vendor_id = v.id\n" +
        "    LEFT JOIN registration_statuses rs on m.registration_id = rs.registration_id\n" +
        "        AND rs.operation = 0\n" +
        "    LEFT JOIN places p on rs.place_registration_id = p.registration_id")
@Getter
@Setter
@NoArgsConstructor
public class MaterialWithDetailsDTO {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "serial_number")
    private String serialNumber;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private StateEquipment state;

    @Column(name = "responsible_id")
    private UUID responsible;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TypeMaterial typeMaterial;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    @Column(name = "registration_id")
    private Long registrationNumber;

    @Column(name = "place_id")
    private UUID placeId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_registration_id")
    private Long placeRegistrationNumber;
}
