package ru.larionov.inventoryservice.converters;

import ru.larionov.inventoryservice.dto.TypeMaterialDTO;
import ru.larionov.inventoryservice.entity.TypeMaterial;

public class TypeMaterialConverter {

    public static TypeMaterialDTO toDTO(TypeMaterial typeMaterial) {
        TypeMaterialDTO typeMaterialDTO = new TypeMaterialDTO();

        typeMaterialDTO.setId(typeMaterial.getId());
        typeMaterialDTO.setName(typeMaterial.getName());
        typeMaterialDTO.setDescription(typeMaterial.getDescription());

        return typeMaterialDTO;
    }

    public static TypeMaterial fromDTO(TypeMaterialDTO typeMaterialDTO) {
        TypeMaterial typeMaterial = new TypeMaterial();

        typeMaterial.setId(typeMaterialDTO.getId());
        typeMaterial.setName(typeMaterialDTO.getName());
        typeMaterial.setDescription(typeMaterialDTO.getDescription());

        return typeMaterial;
    }
}
