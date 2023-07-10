package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import inventory.dtos.VendorDTO;
import ru.larionov.inventoryservice.services.VendorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("inventory/vq/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    public List<VendorDTO> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/{id}")
    public VendorDTO getVendorById(@PathVariable UUID id) {
        return vendorService.getVendorById(id);
    }

    @PostMapping
    public VendorDTO saveVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.saveVendor(vendorDTO);
    }

}
