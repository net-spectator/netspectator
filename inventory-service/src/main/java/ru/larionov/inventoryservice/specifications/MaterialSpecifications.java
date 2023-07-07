package ru.larionov.inventoryservice.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import ru.larionov.inventoryservice.views.DeviceWithDetailsDTO;
import ru.larionov.inventoryservice.views.MaterialWithDetailsDTO;

import java.util.List;
import java.util.UUID;

public class MaterialSpecifications {

    public static Specification<MaterialWithDetailsDTO> placeEqual(UUID place) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("placeId"), place);
    }

    public static Specification<MaterialWithDetailsDTO> placeIn(List<UUID> places) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("placeId"));
            places.forEach(in::value);
            return in;
        };
    }

    public static Specification<MaterialWithDetailsDTO> regNumberEquals(Long regNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("registrationNumber"), regNumber);
    }

    public static Specification<MaterialWithDetailsDTO> nameLike(String namePart) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), namePart + "%");
    }

    public static Specification<MaterialWithDetailsDTO> vendorEqual(UUID vendor) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vendor"), vendor);
    }

    public static Specification<MaterialWithDetailsDTO> vendorLike(String namePart) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("vendorName"), namePart + "%");
    }
}
