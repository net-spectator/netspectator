package org.net.webcoreservice.controllers;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.RouterConverter;
import org.net.webcoreservice.dto.RouterDto;
import org.net.webcoreservice.exeptions.ResourceNotFoundException;
import org.net.webcoreservice.service.RoutersService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routers")
public class RoutersController {

    private final RoutersService routersService;
    private final RouterConverter routerConverter;

    @GetMapping
    public List<RouterDto> getAllRouters() {
        return routersService.findAll().stream().map(routerConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RouterDto getRouterById(@PathVariable Long id) {
        return routerConverter.entityToDto(routersService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Маршрутизатор с id:" + id + " не найден")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewRouter(@RequestBody RouterDto routerDto) {
        routersService.createNewRouter(routerDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRouterById(@PathVariable Long id) {
        routersService.deleteById(id);
    }
}
