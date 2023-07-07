package org.net.webcoreservice.controllers;

import entities.devices.ClientHardwareInfo;
import entities.nodes.DetectedNode;
import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.converters.TrackedEquipmentConverter;
import org.net.webcoreservice.dto.TrackedEquipmentDto;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.net.webcoreservice.exeptions.ResourceNotFoundException;
import org.net.webcoreservice.service.TrackedEquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.NSLogger;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment")
public class TrackedEquipmentController {

    private final TrackedEquipmentService trackedEquipmentService;
    private final TrackedEquipmentConverter converter;

    private final NSLogger LOGGER = new NSLogger(TrackedEquipmentController.class); //добавил логгер

    @GetMapping
    public List<TrackedEquipmentDto> getAllEquipment() {
        return trackedEquipmentService.findAll().stream().map(converter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/hardwareInfo/{id}")
    public ClientHardwareInfo getHardwareInfo(@PathVariable Long id) {
        LOGGER.info("Сработал логгер");
        return trackedEquipmentService.getEquipmentHardwareInfo(id);
    }

    @GetMapping("/broadcastSearch")
    public void search(@RequestParam String ip) {
        trackedEquipmentService.scanNetwork(ip);
    }

    @GetMapping("/nodes")
    public List<DetectedNode> getAllNodes() {
        return trackedEquipmentService.getNodes();
    }

    @PostMapping("/addToScan")
    @ResponseStatus(HttpStatus.CREATED)
    public void addToScan(@RequestParam int index, @RequestParam(required = false) String newName) {
        if (newName.isEmpty()) {
            trackedEquipmentService.addNodesByIndex(index);
        } else {
            trackedEquipmentService.addNodesWithChangeName(index, newName);
        }
    }

    @DeleteMapping("/removeNode/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromNode(@PathVariable int id) {
        trackedEquipmentService.removeNodeFromTrEq(id);
    }

    @PostMapping("/disconnect/{id}")
    public void disconnectClient(@PathVariable Long id) {
        trackedEquipmentService.disconnect(id);
    }

    @GetMapping("/blackList")
    public List<TrackedEquipmentDto> getAllBlackList() {
        return trackedEquipmentService.showBlackList().stream().map(converter::entityToDto).collect(Collectors.toList());
    }

    @PutMapping("/addBlackList/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addClientToBlackList(@PathVariable Long id) {
        trackedEquipmentService.addToBlackList(id);
    }

    @PutMapping("/removeBlackList/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeClientToBlackList(@PathVariable Long id) {
        trackedEquipmentService.removeFromBlackList(id);
    }


    @GetMapping("/{id}")
    public TrackedEquipmentDto getEquipmentById(@PathVariable Long id) {
        return converter.entityToDto(trackedEquipmentService.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Оборудование с id:" + id + " не найдено")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        trackedEquipmentService.deleteById(id);
    }
}
