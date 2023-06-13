package ru.larionov.inventorisationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "registration_statuses")
public class RegistrationStatus {

    @Id
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "registration_id", referencedColumnName = "id")
    private RegistrationNumber registrationNumber;

    @OneToOne
    @JoinColumn(name = "place_registration_id", referencedColumnName = "id")
    private RegistrationNumber placeNumber;

    @Column(name = "operation")
    @Enumerated(EnumType.ORDINAL)
    private RegistrationEventsType eventsType;

    @Column(name = "date")
    private Date date;
}
