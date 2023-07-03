package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.larionov.inventoryservice.converters.VendorConverter;
import ru.larionov.inventoryservice.dto.VendorDTO;
import ru.larionov.inventoryservice.repository.VendorRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll(Sort.by("name"))
                .stream()
                .map(VendorConverter::toDTO)
                .collect(Collectors.toList());
    }

    public VendorDTO getVendorById(UUID id) {
        return VendorConverter.toDTO(vendorRepository.getReferenceById(id));
    }

    public VendorDTO saveVendor(VendorDTO vendorDTO) {
        return VendorConverter.toDTO(vendorRepository.save(
                VendorConverter.fromDTO(vendorDTO)
        ));
    }
}
