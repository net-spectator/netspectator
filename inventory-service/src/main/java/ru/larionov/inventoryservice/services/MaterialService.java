package ru.larionov.inventoryservice.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larionov.inventoryservice.converters.MaterialConverter;
import ru.larionov.inventoryservice.dto.MaterialDTO;
import ru.larionov.inventoryservice.entity.Material;
import ru.larionov.inventoryservice.entity.Place;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.repository.MaterialRepository;
import ru.larionov.inventoryservice.repository.PlaceRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final RegistrationNumberRepository registrationNumberRepository;

    public List<MaterialDTO> getAllMaterials() {
        return materialRepository
                .findAll(Sort.by("name"))
                .stream()
                .map(MaterialConverter::toDTO)
                .collect(Collectors.toList());
    }

    public MaterialDTO getMaterialElement(UUID id) {
        return MaterialConverter.toDTO(
                materialRepository.getReferenceById(id)
        );
    }

    @Transactional
    public MaterialDTO saveMaterial(MaterialDTO materialDTO) {
        Material material = MaterialConverter.fromDTO(materialDTO);
        if (material.getId() == null){
            RegistrationNumber registrationNumber = new RegistrationNumber();
            material.setRegistrationNumber(registrationNumberRepository.save(registrationNumber));
        }

        return MaterialConverter.toDTO(
                materialRepository.save(
                        material));
    }
}
