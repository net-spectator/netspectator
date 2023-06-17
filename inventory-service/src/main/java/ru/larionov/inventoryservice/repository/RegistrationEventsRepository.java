package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larionov.inventoryservice.entity.RegistrationEvent;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegistrationEventsRepository extends JpaRepository<RegistrationEvent, UUID> {

    List<RegistrationEvent> findAllByRegistrationNumber(Long regNumber);
}
