package ru.larionov.inventoryservice.converters;

import inventory.dtos.VendorDTO;
import ru.larionov.inventoryservice.entity.Vendor;

public class VendorConverter {

    public static VendorDTO toDTO(Vendor vendor) {
        VendorDTO vendorDTO = new VendorDTO();

        vendorDTO.setId(vendor.getId());
        vendorDTO.setName(vendor.getName());

        return vendorDTO;
    }

    public static Vendor fromDTO(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();

        vendor.setId(vendorDTO.getId());
        vendor.setName(vendorDTO.getName());

        return vendor;
    }
}
