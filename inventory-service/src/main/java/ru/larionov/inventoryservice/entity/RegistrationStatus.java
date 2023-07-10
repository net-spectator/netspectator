package ru.larionov.inventoryservice.entity;

import inventory.enums.RegistrationEventsType;
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
    @Column(name = "registration_id")
    private Long registrationNumber;

    @Column(name = "place_registration_id")
    private Long placeNumber;

    @Column(name = "operation")
    @Enumerated(EnumType.ORDINAL)
    private RegistrationEventsType eventsType;

    @Column(name = "date")
    private Date date;
}
