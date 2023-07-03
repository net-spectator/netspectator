package services;

import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
                .executor_comments(requestsDto.getExecutorComments())
                .request_status(requestsDto.getRequestsStatus())
                .request_user_id(requestsDto.getRequestUserId())
                .request_user_id(requestsDto.getRequestUserId())
                .equipment_id(requestsDto.getEquipmentId())
                .build();
        requestsRepository.save(requests);
        return requests;
    }

    public Page<Requests> findAll() {
        return (Page<Requests>) requestsStatusRepository.findAll();
    }
}
