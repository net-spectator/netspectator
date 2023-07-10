package ru.larionov.inventoryservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.larionov.inventoryservice.dto.UserCache;
import users.dtos.RoleDTO;
import users.dtos.UserDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${user_service.host}")
    private String host;

    @Value("${user_service.port}")
    private String port;
    private static final String URL_GET_USERS = "/api/v1/users";
    private static final Long CACHE_LIVE = 1_200_000L; //20 minutes

    private final WebClient webClient;

    private Map<UUID, UserCache> cacheMap = new HashMap<>();

    public boolean isAdmin(UUID userId) {
        UserDTO userDTO = getUserDetails(userId);

        return userDTO.getRoles()
                .stream()
                .map(RoleDTO::getTitle)
                .anyMatch(t -> t.equals("ROLE_ADMIN"));
    }

    private String getHostAddress() {
        return host + ":" + port;
    }

    private UserDTO getUserDetails(UUID userId) {
        Long time = System.currentTimeMillis();
        if (cacheMap.containsKey(userId)) {
            if (cacheMap.get(userId).getTimeToLive() <= time)
                return cacheMap.get(userId).getUserDTO();
        }
        UserDTO userDTO = webClient.get()
                .uri(getHostAddress() + URL_GET_USERS + userId.toString())
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
        cacheMap.put(userId, new UserCache(time + CACHE_LIVE, userDTO));
        return userDTO;
    }

}
