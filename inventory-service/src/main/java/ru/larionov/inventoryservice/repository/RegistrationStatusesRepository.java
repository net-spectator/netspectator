package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larionov.inventoryservice.entity.RegistrationStatus;

@Repository
public interface RegistrationStatusesRepository extends JpaRepository<RegistrationStatus, Long> {

}
