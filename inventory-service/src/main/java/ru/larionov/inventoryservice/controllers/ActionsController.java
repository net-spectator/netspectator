package ru.larionov.inventoryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.larionov.inventoryservice.dto.PairRegistrationNumbers;
import inventory.dtos.RegistrationDataDTO;
import http.dtos.Response;
import ru.larionov.inventoryservice.services.InventoryActionsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("inventory/v1/actions")
@RequiredArgsConstructor
public class ActionsController {

    private final InventoryActionsService actionsService;

    @GetMapping("/get_registration_status/{id}")
    public RegistrationDataDTO getRegistrationDataById(@PathVariable Long id) {
        return actionsService.getRegistrationDataById(id);
    }

    @GetMapping("/get_registration_history/{id}")
    public List<RegistrationDataDTO> getRegistrationHistory(@PathVariable Long id) {
        return actionsService.getHistoryInventoryActions(id);
    }

    @PostMapping("/add")
    public Response addIntoPlace(@RequestBody PairRegistrationNumbers pairRegistrationNumbers) {
        actionsService.addIntoPlace(pairRegistrationNumbers.getObjectId(), pairRegistrationNumbers.getPlaceId());

        return new Response(LocalDateTime.now(), "ok", false);
    }
}
