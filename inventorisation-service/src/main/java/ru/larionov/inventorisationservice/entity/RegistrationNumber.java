package ru.larionov.inventorisationservice.entity;

import jakarta.persistence.*;
import liquibase.diff.output.changelog.core.UnexpectedUniqueConstraintChangeGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "registration_numbers")
public class RegistrationNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
