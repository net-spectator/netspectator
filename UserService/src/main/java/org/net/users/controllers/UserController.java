package org.net.users.controllers;

import lombok.RequiredArgsConstructor;
import org.net.users.converters.UserConverter;
import org.net.users.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import users.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserConverter userConverter;

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{uuid}")
    public UserDTO getUser(@PathVariable UUID uuid) {
        return userConverter.toDTO(userService.getUser(uuid).get());
    }
}
