package org.net.users.controllers;

import lombok.RequiredArgsConstructor;
import org.net.users.converters.UserConverter;
import org.net.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.dtos.RoleDTO;
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
    public ResponseEntity<List<UserDTO>> getUsers(@RequestHeader(value = "x-introspect", required = false) UUID userID) {
        if (checkRole(userID)) {
            return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("{uuid}")
    public ResponseEntity<UserDTO> getUser(@RequestHeader(value = "x-introspect", required = false) UUID userID, @PathVariable UUID uuid) {
        if (checkRole(userID) || userID.equals(uuid)) {
            return new ResponseEntity<>(userConverter.toDTO(userService.getUser(uuid).get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public Boolean checkRole(UUID userID) {
        UserDTO user = userConverter.toDTO(userService.getUser(userID).get());
        RoleDTO role_admin = user.getRoles().stream().filter(r -> r.getTitle().equals("ROLE_ADMIN")).findFirst().orElse(null);
        return null != role_admin;
    }
}
