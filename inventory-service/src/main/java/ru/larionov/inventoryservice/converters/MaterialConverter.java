package ru.larionov.inventoryservice.converters;

import inventory.dtos.MaterialDTO;
import ru.larionov.inventoryservice.entity.Material;
import ru.larionov.inventoryservice.entity.RegistrationNumber;
import ru.larionov.inventoryservice.views.MaterialWithDetailsDTO;

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
        materialDTO.setTypeMaterial(TypeMaterialConverter.toDTO(material.getTypeMaterial()));
        materialDTO.setVendor(VendorConverter.toDTO(material.getVendor()));
        materialDTO.setRegistrationNumber(material.getRegistrationNumber().getId());

        return materialDTO;
    }

    public static MaterialDTO toDTO(MaterialWithDetailsDTO material) {
        MaterialDTO materialDTO = new MaterialDTO();

        materialDTO.setId(material.getId());
        materialDTO.setName(material.getName());
        materialDTO.setDescription(material.getDescription());
        materialDTO.setSerialNumber(material.getSerialNumber());
        materialDTO.setState(material.getState());
        materialDTO.setResponsible(material.getResponsible());
        materialDTO.setTypeMaterial(TypeMaterialConverter.toDTO(material.getTypeMaterial()));
        materialDTO.setVendor(VendorConverter.toDTO(material.getVendor()));
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
        material.setTypeMaterial(TypeMaterialConverter.fromDTO(materialDTO.getTypeMaterial()));
        material.setVendor(VendorConverter.fromDTO(materialDTO.getVendor()));
        material.setRegistrationNumber(new RegistrationNumber(materialDTO.getRegistrationNumber()));

        return material;
    }
}
