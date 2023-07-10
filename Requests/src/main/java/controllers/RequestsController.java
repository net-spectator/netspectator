package controllers;

import converters.RequestsConverter;
import entities.Requests;
import entities.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.DeviceDTO;
import services.RequestsService;
import users.dtos.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;
    private final RequestsConverter requestsConverter;

    @GetMapping
    public List<RequestsDto> getUserRequests(@RequestHeader String username) {
        return requestsService.findUserRequests(username).stream().map(requestsConverter::requestsToDto).collect(Collectors.toList());
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRequest (@RequestHeader String username,@RequestHeader String equipment, @RequestHeader String topic,@RequestHeader String description){
        requestsService.createNewRequests(username, equipment,topic, description);
    }

    @GetMapping
    public List<RequestsDto> findRequests(
            @RequestParam(required = false, name = "title") UserDTO user,
            @RequestParam(required = false, name = "equipment") DeviceDTO equipment,
            @RequestParam(required = false, name = "topic") String topic,
            @RequestParam(defaultValue = "1", name = "p")Integer page
    ){
        if (page<1) { page  =1; }
        Specification<Requests> spec = requestsService.createSpecByFilters(user, equipment, topic);
        return requestsService.findAll(spec,page-1).map(requestsConverter::requestsToDto).getContent();
    }
}