package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import users.dtos.UserDTO;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String URL_GET_USERS = "localhost:8080/api/v1/users";

    private final WebClient webClient;

    private UserDTO getUserDetails(UUID userId) {
        return webClient.get()
                .uri(URL_GET_USERS)
                .a
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

}
