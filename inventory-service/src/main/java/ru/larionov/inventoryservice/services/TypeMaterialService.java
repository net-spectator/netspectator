package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.larionov.inventoryservice.converters.TypeMaterialConverter;
import ru.larionov.inventoryservice.dto.TypeMaterialDTO;
import ru.larionov.inventoryservice.repository.TypeMaterialRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeMaterialService {

    private final TypeMaterialRepository typeMaterialRepository;


    public List<TypeMaterialDTO> getAllTypesMaterial() {
        return typeMaterialRepository.findAll(
                Sort.by("name")
        ).stream()
                .map(TypeMaterialConverter::toDTO)
                .collect(Collectors.toList());
    }

    public TypeMaterialDTO getTypeMaterialById(UUID id) {
        return TypeMaterialConverter.toDTO(typeMaterialRepository.getReferenceById(id));
    }

    public TypeMaterialDTO saveTypeMaterialObject(TypeMaterialDTO typeMaterialDTO) {
        return TypeMaterialConverter.toDTO(typeMaterialRepository.save(
                TypeMaterialConverter.fromDTO(typeMaterialDTO)
        ));
    }
}
