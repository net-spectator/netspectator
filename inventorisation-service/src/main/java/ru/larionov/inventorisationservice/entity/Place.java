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
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Place parent;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "registration_id", referencedColumnName = "id")
    private RegistrationNumber registrationNumber;
}
