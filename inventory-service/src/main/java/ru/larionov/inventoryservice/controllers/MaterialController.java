package ru.larionov.inventoryservice.controllers;

import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.MaterialDTO;
import ru.larionov.inventoryservice.services.MaterialService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping()
    public List<MaterialDTO> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public MaterialDTO getMaterialElement(@PathVariable UUID id) {
        return materialService.getMaterialElement(id);
    }

    @PostMapping
    public MaterialDTO createMaterial(@RequestBody MaterialDTO material) {
        return materialService.saveMaterial(material);
    }
}
