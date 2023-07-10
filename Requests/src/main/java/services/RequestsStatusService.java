package services;

import entities.RequestsStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repositories.RequestsStatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestsStatusService {
    private final RequestsStatusRepository requestsStatusRepository;
    public Optional<RequestsStatus>findByTitle(String title){
        return requestsStatusRepository.findByTitle(title);
    }
}
