package ru.larionov.inventoryservice.services;

import amq.entities.Message;
import amq.entities.NotificationMessage;
import amq.services.RabbitMQProducerService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.larionov.inventoryservice.converters.DeviceConverter;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import ru.larionov.inventoryservice.entity.*;
import ru.larionov.inventoryservice.exeptions.BadParametersOfRequest;
import ru.larionov.inventoryservice.exeptions.TypeMaterialNotFound;
import ru.larionov.inventoryservice.exeptions.VendorNotFound;
import ru.larionov.inventoryservice.repository.*;
import ru.larionov.inventoryservice.specifications.DeviceSpecifications;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;
import utils.NSLogger;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceWithDetailsRepository deviceWithDetailsRepository;
    private final RegistrationNumberRepository registrationNumberRepository;
    private final TypeMaterialRepository typeMaterialRepository;
    private final UserService userService;
    private final VendorRepository vendorRepository;
    private final NotificationService notificationService;

    private final NSLogger LOGGER = new NSLogger(Device.class);

    @PostConstruct
    private void init() {
        LOGGER.setLocalLog(true);
        LOGGER.setServerLog(true);
    }

    public List<DeviceDTO> getAllDevices(UUID userID,
                                         Integer page,
                                         Integer size,
                                         UUID place,
                                         Boolean hierarchy,
                                         Long number,
                                         String namePart,
                                         UUID vendor) {

        size = size > 50 ? 50 : size;

        Specification<DeviceWithDetailsDTO> spec = Specification.where(null);

        if (!userService.isAdmin(userID)){
            spec = spec.and(
                    DeviceSpecifications
                            .userEqual(userID)
                            .or(
                                    DeviceSpecifications.responsibleEqual(userID)));
        }

        if (place != null){
            if (hierarchy){
                spec = spec.and(DeviceSpecifications.placeIn(List.of(place)));
            }
            else
                spec = spec.and(DeviceSpecifications.placeEqual(place));
        }

        if (number != null) {
            spec = spec.and(DeviceSpecifications.regNumberEquals(number));
        }

        if (vendor != null) {
            spec = spec.and(DeviceSpecifications.vendorEqual(vendor));
        }

        if (namePart != null) {
            spec = spec.and(DeviceSpecifications.nameLike(namePart)
                    .or(DeviceSpecifications.vendorLike(namePart))
            );
        }

        return deviceWithDetailsRepository.findAll(spec,
                        PageRequest.of(page -1,
                                size,
                                Sort.by("registrationNumber")
                        )
                )
                .stream()
                .map(DeviceConverter::toDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(UUID id) {
        return DeviceConverter.toDTO(
                deviceRepository.getReferenceById(id)
        );
    }

    @Transactional
    public DeviceDTO saveDevice(DeviceDTO deviceDTO) {

        Device device = DeviceConverter.fromDTO(deviceDTO);

        boolean isNew = device.getId() == null;

        Vendor vendor = device.getVendor();
        if (vendor != null){
            if (vendor.getId() != null) {
                Optional<Vendor> vendorById = vendorRepository.findById(vendor.getId());
                if (vendorById.isEmpty()){
                    LOGGER.error("Vendor not found " + vendor.getId().toString());
                    throw new VendorNotFound(vendor.getId().toString());
                }
                else
                    device.setVendor(vendorById.get());
            } else {
                LOGGER.error("passed non-existent attribute in base \"Vendor\"");
                throw new BadParametersOfRequest("passed non-existent attribute in base \"Vendor\"");
            }
        }

        TypeMaterial typeMaterial = device.getTypeMaterial();
        if (typeMaterial != null) {
            if (typeMaterial.getId() != null) {
                Optional<TypeMaterial> typeMaterialById = typeMaterialRepository.findById(typeMaterial.getId());
                if (typeMaterialById.isEmpty()) {
                    LOGGER.error("Type material not found " + vendor.getId().toString());
                    throw new TypeMaterialNotFound(typeMaterial.getId().toString());
                }
                else
                    device.setTypeMaterial(typeMaterialById.get());
            } else {
                LOGGER.error("passed non-existent attribute in base \"TypeMaterial\"");
                throw new BadParametersOfRequest("passed non-existent attribute in base \"TypeMaterial\"");
            }
        }


        if (device.getId() == null){
            RegistrationNumber registrationNumber = new RegistrationNumber();
            device.setRegistrationNumber(registrationNumberRepository.save(registrationNumber));
        } else {
            Device dbDevice = deviceRepository.findById(device.getId()).get();
            if (!dbDevice.getUser().equals(device.getUser())) {
                LOGGER.info(String.format("изменен пользователь у устройства %s [%s]",
                        device.getName(), device.getId()));
                notificationService.sendNotification(6L,
                        String.format("Сменился пользователь у устройства %s [%s]",
                                device.getName(),
                                device.getId()));
            }
        }

        LOGGER.info(String.format("Сохранено/изменено устройство %s [%s]",
                device.getName(), device.getId()));
        return DeviceConverter.toDTO(deviceRepository.save(device));
    }
}
