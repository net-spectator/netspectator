package services;

import entities.Requests;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import repositories.RequestsRepository;
import repositories.RequestsStatusRepository;
import javax.transaction.Transactional;

import repositories.specifications.RequestsSpecifications;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import users.dtos.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ch.qos.logback.core.joran.JoranConstants.NULL;

@Service
@RequiredArgsConstructor
public class RequestsService {
    private final RequestsRepository requestsRepository;
    private final RequestsStatusRepository requestsStatusRepository;

    public Optional<Requests> findById(Long id) {
        return requestsRepository.findById(id);
    }

    public void  deleteById(Long id) {
        requestsRepository.deleteById(id);
    }

    @Transactional
    public void createNewRequests (String user, String equipment,String topic,String description){
        Requests req = new Requests();
        req.setUserId(UUID.fromString(user));

        req.setReqStatusId(UUID.fromString("Создано"));
        req.setTopic(topic);
        req.setDescription(description);
        req.setExecutorUserId(UUID.fromString(NULL));
        req.setExecutorComments(NULL);

        requestsRepository.save(req);
    }

    public Page<Requests> findAll(Specification<Requests> spec, int page) {
        int sizePage = 10;
        return requestsRepository.findAll(spec, PageRequest.of(page,sizePage));
    }

    public List<Requests> findUserRequests(String username) {
        return requestsRepository.findAllByUsername(username);
    }

    public Specification<Requests> createSpecByFilters(UserDTO user, DeviceDTO equipment, String topic){
        Specification<Requests> spec = Specification.where(null);
        if (user != null){
            spec = spec.and(RequestsSpecifications.usersThan(user));
        }
        if (equipment != null){
            spec = spec.and(RequestsSpecifications.equipmentThan(equipment));
        }
        if (topic != null){
            spec = spec.and(RequestsSpecifications.topicLike(topic));
        }
        return spec;
    }
}
