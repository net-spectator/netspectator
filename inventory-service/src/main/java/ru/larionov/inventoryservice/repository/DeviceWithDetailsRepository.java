package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;

import java.util.UUID;

@Repository
public interface DeviceWithDetailsRepository extends JpaRepository<DeviceWithDetailsDTO, UUID>, JpaSpecificationExecutor<DeviceWithDetailsDTO> {
}
