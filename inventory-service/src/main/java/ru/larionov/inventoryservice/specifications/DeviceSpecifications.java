package ru.larionov.inventoryservice.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;

import java.util.List;
import java.util.UUID;

public class DeviceSpecifications {

    public static Specification<DeviceWithDetailsDTO> placeEqual(UUID place) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("placeId"), place);
    }

    public static Specification<DeviceWithDetailsDTO> placeIn(List<UUID> places) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("placeId"));
            places.forEach(in::value);
            return in;
        };
    }

    public static Specification<DeviceWithDetailsDTO> regNumberEquals(Long regNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("registrationNumber"), regNumber);
    }

    public static Specification<DeviceWithDetailsDTO> nameLike(String namePart) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), namePart + "%");
    }

    public static Specification<DeviceWithDetailsDTO> vendorEqual(UUID vendor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vendor"), vendor);
    }

    public static Specification<DeviceWithDetailsDTO> vendorLike(String namePart) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("vendorName"), namePart + "%");
    }

    public static Specification<DeviceWithDetailsDTO> userEqual(UUID userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<DeviceWithDetailsDTO> responsibleEqual(UUID responsibleId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("responsibleId"), responsibleId);
    }
}
