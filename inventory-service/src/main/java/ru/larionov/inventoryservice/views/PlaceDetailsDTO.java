package ru.larionov.inventoryservice.views;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;
import ru.larionov.inventoryservice.entity.TypePlace;

@Entity
@Immutable
@Subselect("SELECT\n" +
        "    d.registration_id AS place_id,\n" +
        "    d.name AS place_name,\n" +
        "    0 AS place_type\n" +
        "FROM devices d\n" +
        "WHERE d.registration_id = :place_id\n" +
        "UNION\n" +
        "SELECT\n" +
        "    p.registration_id,\n" +
        "    p.name,\n" +
        "    1\n" +
        "FROM places p\n" +
        "WHERE p.registration_id = :place_id\n" +
        "UNION \n" +
        "SELECT \n" +
        "    m.registration_id,\n" +
        "    m.name,\n" +
        "    2\n" +
        "FROM materials m"
)
@Getter
@Setter
@NoArgsConstructor
public class PlaceDetailsDTO {

    @Id
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "place_name")
    private String name;

    @Column(name = "place_type")
    @Enumerated(EnumType.ORDINAL)
    private TypePlace typePlace;
}
