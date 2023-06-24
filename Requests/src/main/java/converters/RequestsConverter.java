package converters;

import entities.Requests;
import entities.RequestsStatus;
import entities.devices.requests.RequestsDto;
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
                requests.getRequest_user_id(),
                requests.getEquipment_id(),
                requests.getExecutor_user_id(),
                requests.getExecutor_comments(),
                requests.getRequest_status());
    }

    public Requests dtoToEntity(RequestsDto requestsDto){
        Requests req = new Requests();
        req.setId(requestsDto.getId());
        req.setTopic(requestsDto.getTopic());
        req.setDescription(requestsDto.getDescription());
        req.setRequest_user_id(requestsDto.getRequest_user_id());
        req.setEquipment_id(requestsDto.getEquipment_id());
        req.setExecutor_user_id(requestsDto.getExecutor_user_id());
        req.setExecutor_comments(requestsDto.getExecutor_comments());
        req.setRequest_status(requestsDto.getRequest_status());
        return req;
    }
}
