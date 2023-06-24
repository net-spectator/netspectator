package services;

import entities.Requests;
import entities.devices.requests.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import repositories.RequestsRepository;
import repositories.RequestsStatusRepository;
import java.util.Optional;

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

    public Requests createNewRequests (RequestsDto requestsDto){
        Requests requests = Requests.newBuilder()
                .topic(requestsDto.getTopic())
                .description(requestsDto.getDescription())
                .executor_comments(requestsDto.getExecutor_comments())
                .request_status(requestsDto.getRequest_status())
                .request_user_id(requestsDto.getRequest_user_id())
                .request_user_id(requestsDto.getRequest_user_id())
                .equipment_id(requestsDto.getEquipment_id())
                .build();
        requestsRepository.save(requests);
        return requests;
    }

    public Page<Requests> findAll() {
        return (Page<Requests>) requestsStatusRepository.findAll();
    }
}
