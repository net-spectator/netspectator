package org.net.usermanage.controllers;

import lombok.RequiredArgsConstructor;
import org.net.usermanage.data.User;
import org.net.usermanage.services.UserActualizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserActualizeService userActualizeService;

    @GetMapping
    public List<User> getUsers() {
        return userActualizeService.getUsers();
    }

    @GetMapping("{uuid}")
    public User getUser(@PathVariable UUID uuid) {
        return userActualizeService.getUser(uuid).get();
    }
}
