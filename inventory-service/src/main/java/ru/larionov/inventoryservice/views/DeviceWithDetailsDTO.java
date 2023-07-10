package ru.larionov.inventoryservice.views;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
import ru.larionov.inventoryservice.entity.*;

import java.util.UUID;

@Entity
@Immutable
@Subselect("SELECT\n" +
        "    d.id,\n" +
        "    d.name,\n" +
        "    d.description,\n" +
        "    d.state,\n" +
        "    d.responsible_id,\n" +
        "    d.user_id,\n" +
        "    d.type_id,\n" +
        "    d.vendor_id,\n" +
        "    d.registration_id,\n" +
        "    p.id AS place_id,\n" +
        "    p.name AS place_name,\n" +
        "    p.registration_id AS place_registration_id,\n" +
        "    v.name AS vendor_name\n" +
        "FROM devices d\n" +
        "LEFT JOIN registration_statuses rs on d.registration_id = rs.registration_id\n" +
        "        AND rs.operation = 0\n" +
        "LEFT JOIN places p on rs.registration_id = p.registration_id\n" +
        "LEFT JOIN vendors v on d.vendor_id = v.id")
@NoArgsConstructor
@Setter
@Getter
public class DeviceWithDetailsDTO {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private StateEquipment state;

    @Column(name = "responsible_id")
    private UUID responsibleId;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TypeMaterial typeMaterial;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "registration_id")
    private Long registrationNumber;

    @Column(name = "place_id")
    private UUID placeId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_registration_id")
    private Long placeRegistrationId;
}
