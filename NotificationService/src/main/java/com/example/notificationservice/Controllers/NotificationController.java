package com.example.notificationservice.Controllers;

import com.example.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final UserService userService;

    @GetMapping
    public List<UUID> getAllUuid() {
        return userService.getUuidFromUsers();
    }

    @PostMapping
    public void writeUuidToDb(@RequestParam UUID uuid, Long typeId) {
        userService.insertUserIntoDb(uuid, typeId);
    }
}
