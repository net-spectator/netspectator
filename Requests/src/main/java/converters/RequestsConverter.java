package converters;

import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import ru.larionov.inventoryservice.entity.Device;

@Component
@RequiredArgsConstructor
public class RequestsConverter {
    public static Device entityfromDTO(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setName(deviceDTO.getName());
        device.setDescription(deviceDTO.getName());
        device.setResponsible(deviceDTO.getResponsible());
        device.setState(deviceDTO.getState());
        device.setRegistrationNumber(deviceDTO.getRegistrationNumber());
        device.setTypeMaterial(deviceDTO.getTypeMaterial());
        device.setVendor(deviceDTO.getVendor());
        device.setUser(deviceDTO.getUser());
        return device;
    }
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
