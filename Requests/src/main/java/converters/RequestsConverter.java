package converters;

import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RequestsConverter {
    public RequestsDto requestsToDto (Requests requests){
        RequestsDto requestsDto = new RequestsDto();
        requestsDto.setReqId(requests.getReqId());
        requestsDto.setTopic(requests.getTopic());
        requestsDto.setDescription(requests.getDescription());
        requestsDto.setUserId(requests.getUserId());
        requestsDto.setEquipmentId(requests.getEquipmentId());
        requestsDto.setExecutorUserId(requests.getExecutorUserId());
        requestsDto.setExecutorComments(requests.getExecutorComments());
        requestsDto.setReqStatusId(requests.getReqStatusId());

        return requestsDto;
    }

    public Requests dtoToEntity(RequestsDto requestsDto){
        Requests req = new Requests();
        req.setReqId(requestsDto.getReqId());
        req.setTopic(requestsDto.getTopic());
        req.setDescription(requestsDto.getDescription());
        req.setUserId(requestsDto.getUserId());
        req.setEquipmentId(requestsDto.getEquipmentId());
        req.setExecutorUserId(requestsDto.getExecutorUserId());
        req.setExecutorComments(requestsDto.getExecutorComments());
        req.setReqStatusId(requestsDto.getReqStatusId());
        return req;
    }
}
