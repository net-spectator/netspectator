package controllers;

import entities.Requests;
import entities.devices.requests.RequestsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import services.RequestsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;
    private final RequestsController requestsConverter;

    private RequestsDto entityToTdo(Requests p) {
        RequestsDto requestsDto = new RequestsDto();
        return requestsDto;
    }

    @GetMapping
    public List<RequestsDto> findRequests(){
        return requestsService.findAll().map(requestsConverter::entityToTdo).getContent();
    }
}