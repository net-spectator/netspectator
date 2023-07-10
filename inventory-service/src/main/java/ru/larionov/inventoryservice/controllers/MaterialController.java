package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.MaterialDTO;
import ru.larionov.inventoryservice.services.MaterialService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping()
    public List<MaterialDTO> getAllMaterials(@RequestHeader("x-introspect") UUID userID,
                                             @RequestParam(name = "page", defaultValue = "1") Integer page,
                                             @RequestParam(name = "size", defaultValue = "30") Integer size,
                                             @RequestParam(name = "place_id", required = false) UUID place,
                                             @RequestParam(name = "hierarchy", defaultValue = "false") Boolean hierarchy,
                                             @RequestParam(name = "reg_number", required = false) Long number,
                                             @RequestParam(name = "name_part", required = false) String namePart,
                                             @RequestParam(name = "vendor_id", required = false) UUID vendor) {
        return materialService.getAllMaterials(userID, page, size, place, hierarchy, number, namePart, vendor);
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
