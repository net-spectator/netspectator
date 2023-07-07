package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.larionov.inventoryservice.views.MaterialWithDetailsDTO;

public interface MaterialWithDetailsRepository extends JpaSpecificationExecutor<MaterialWithDetailsDTO> {
}
