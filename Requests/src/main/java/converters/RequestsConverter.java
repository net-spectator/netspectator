package converters;

import entities.Requests;
import entities.RequestsStatus;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestsConverter {
    private final RequestsStatus requestsStatus;
    public RequestsDto requestsToDto (Requests requests){
        return new RequestsDto(
                requests.getId(),
                requests.getTopic(),
                requests.getDescription(),
                requests.getRequestUserId(),
                requests.getEquipmentId(),
                requests.getExecutorUserId(),
                requests.getExecutorComments(),
                requests.getRequestsStatus());
    }

    public Requests dtoToEntity(RequestsDto requestsDto){
        Requests req = new Requests();
        req.setId(requestsDto.getId());
        req.setTopic(requestsDto.getTopic());
        req.setDescription(requestsDto.getDescription());
        req.setRequestUserId(requestsDto.getRequestUserId());
        req.setEquipmentId(requestsDto.getEquipmentId());
        req.setExecutorUserId(requestsDto.getExecutorUserId());
        req.setExecutorComments(requestsDto.getExecutorComments());
        req.setRequestsStatus(requestsDto.getRequestsStatus());
        return req;
    }
}
