package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import inventory.dtos.TypeMaterialDTO;
import ru.larionov.inventoryservice.services.TypeMaterialService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/type_materials")
@RequiredArgsConstructor
public class TypeMaterialController {

    private final TypeMaterialService typeMaterialService;


    @GetMapping
    public List<TypeMaterialDTO> getAllTypesMaterial() {
        return typeMaterialService.getAllTypesMaterial();
    }

    @GetMapping("/{id}")
    public TypeMaterialDTO getTypeMaterialById(@PathVariable UUID id) {
        return typeMaterialService.getTypeMaterialById(id);
    }

    @PostMapping
    public TypeMaterialDTO saveTypeMaterial(@RequestBody TypeMaterialDTO typeMaterialDTO) {
        return typeMaterialService.saveTypeMaterialObject(typeMaterialDTO);
    }
}
