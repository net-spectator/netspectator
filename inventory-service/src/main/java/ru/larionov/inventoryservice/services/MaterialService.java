package ru.larionov.inventoryservice.services;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.larionov.inventoryservice.converters.DeviceConverter;
import ru.larionov.inventoryservice.converters.MaterialConverter;
import ru.larionov.inventoryservice.dto.MaterialDTO;
import ru.larionov.inventoryservice.entity.Material;
import ru.larionov.inventoryservice.entity.Place;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.repository.MaterialRepository;
import ru.larionov.inventoryservice.repository.MaterialWithDetailsRepository;
import ru.larionov.inventoryservice.repository.PlaceRepository;
import ru.larionov.inventoryservice.repository.RegistrationNumberRepository;
import ru.larionov.inventoryservice.specifications.DeviceSpecifications;
import ru.larionov.inventoryservice.specifications.MaterialSpecifications;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;
import ru.larionov.inventoryservice.views.MaterialWithDetailsDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final RegistrationNumberRepository registrationNumberRepository;
    private final MaterialWithDetailsRepository materialWithDetailsRepository;
    private final UserService userService;

    public List<MaterialDTO> getAllMaterials(UUID userID,
                                             Integer page,
                                             Integer size,
                                             UUID place,
                                             Boolean hierarchy,
                                             Long number,
                                             String namePart,
                                             UUID vendor) {
        size = size > 50 ? 50 : size;



        Specification<MaterialWithDetailsDTO> spec = Specification.where(null);

        if (!userService.isAdmin(userID)){
            spec = spec.and(
                    MaterialSpecifications
                            .userEqual(userID)
                            .or(
                                    MaterialSpecifications.responsibleEqual(userID)));
        }

        if (place != null){
            if (hierarchy){
                spec = spec.and(MaterialSpecifications.placeIn(List.of(place)));
            }
            else
                spec = spec.and(MaterialSpecifications.placeEqual(place));
        }

        if (number != null) {
            spec = spec.and(MaterialSpecifications.regNumberEquals(number));
        }

        if (vendor != null) {
            spec = spec.and(MaterialSpecifications.vendorEqual(vendor));
        }

        if (namePart != null) {
            spec = spec.and(MaterialSpecifications.nameLike(namePart)
                    .or(MaterialSpecifications.vendorLike(namePart))
            );
        }

        return materialWithDetailsRepository.findAll(spec,
                        PageRequest.of(page -1,
                                size,
                                Sort.by("registrationNumber")
                        )
                )
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
