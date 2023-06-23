package ru.larionov.inventoryservice.converters;

import ru.larionov.inventoryservice.dto.MaterialDTO;
import ru.larionov.inventoryservice.entity.Material;

public class MaterialConverter {

    // TODO: add ability to fill a userDTO data into the field "responsible"
    public static MaterialDTO toDTO(Material material) {
        MaterialDTO materialDTO = new MaterialDTO();

        materialDTO.setId(material.getId());
        materialDTO.setName(material.getName());
        materialDTO.setDescription(material.getDescription());
        materialDTO.setSerialNumber(material.getSerialNumber());
        materialDTO.setState(material.getState());
        materialDTO.setResponsible(material.getResponsible());
        materialDTO.setTypeMaterial(material.getTypeMaterial());
        materialDTO.setVendor(material.getVendor());
        materialDTO.setRegistrationNumber(material.getRegistrationNumber());

        return materialDTO;
    }

    // TODO: add ability to get the userID field from userDTO
    public static Material fromDTO(MaterialDTO materialDTO){
        Material material = new Material();

        material.setId(materialDTO.getId());
        material.setName(materialDTO.getName());
        material.setDescription(materialDTO.getDescription());
        material.setSerialNumber(materialDTO.getSerialNumber());
        material.setState(materialDTO.getState());
        material.setResponsible(materialDTO.getResponsible());
        material.setTypeMaterial(materialDTO.getTypeMaterial());
        material.setVendor(materialDTO.getVendor());
        material.setRegistrationNumber(materialDTO.getRegistrationNumber());

        return material;
    }
}
