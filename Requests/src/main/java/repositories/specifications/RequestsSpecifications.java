package repositories.specifications;

import entities.Requests;
import org.springframework.data.jpa.domain.Specification;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import users.dtos.UserDTO;

public class RequestsSpecifications {
    public static Specification <Requests> usersThan (UserDTO user) {
        return (root, criteriaQuery,criteriaBuilder) -> criteriaBuilder.like(root.get("User"),String.format("%%%s%%", user));
    }

    public static Specification <Requests> equipmentThan (DeviceDTO equipment) {
        return (root, criteriaQuery,criteriaBuilder) -> criteriaBuilder.like(root.get("User"),String.format("%%%s%%", equipment));
    }

    public static Specification <Requests> topicLike  (String topic ) {
        return (root, criteriaQuery,criteriaBuilder) -> criteriaBuilder.like(root.get("topic"), String.format("%%%s%%",topic));
    }

}
