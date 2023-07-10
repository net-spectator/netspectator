package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.larionov.inventoryservice.views.MaterialWithDetailsDTO;

import java.util.UUID;

public interface MaterialWithDetailsRepository extends JpaRepository<MaterialWithDetailsDTO, UUID>, JpaSpecificationExecutor<MaterialWithDetailsDTO> {
}
