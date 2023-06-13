package ru.larionov.inventorisationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private StateEquipment state;

    @Column(name = "responsible")
    private UUID responsible;

    @Column(name = "user")
    private UUID user;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TypeMaterial typeMaterial;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Vendor vendor;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "registration_id", referencedColumnName = "id")
    private RegistrationNumber registrationNumber;
}
