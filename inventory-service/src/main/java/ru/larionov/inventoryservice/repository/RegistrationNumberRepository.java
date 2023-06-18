package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larionov.inventoryservice.entity.RegistrationNumber;

@Repository
public interface RegistrationNumberRepository extends JpaRepository<RegistrationNumber, Long> {
}
