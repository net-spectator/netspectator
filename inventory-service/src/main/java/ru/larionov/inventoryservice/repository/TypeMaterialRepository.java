package ru.larionov.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.larionov.inventoryservice.entity.TypeMaterial;

import java.util.UUID;

@Repository
public interface TypeMaterialRepository extends JpaRepository<TypeMaterial, UUID> {
}
