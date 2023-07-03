package com.example.notificationservice.service;

import com.example.notificationservice.Converters.GroupConverter;
import com.example.notificationservice.Dto.GroupDto;
import com.example.notificationservice.Repository.GroupsRepository;
import com.example.notificationservice.entity.Groups;
import com.example.notificationservice.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import users.dtos.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final GroupsRepository groupsRepository;
    private final GroupConverter converter;


    public List<UUID> getUuidFromUsers() {
        String url = "http://localhost:9090/api/v1/users";
        ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDTO>>() {}
        );
        List<UserDTO> userDTOList = response.getBody();
        List<UUID> uuids = userDTOList.stream().map(UserDTO::getUuid).collect(Collectors.toList());
        return uuids;
    }

    public void insertUserIntoDb(UUID uuid, Long errorType) {
        NotificationType type = new NotificationType();
        type.setId(errorType);
        Groups groups = new Groups();
        groups.setUserUuid(uuid);
        groups.setTypeId(type);
        groupsRepository.save(groups);
    }

    public List<String> getUsersEmail(Long errorType) {
        List<GroupDto> groupDtos = getUsersUuidFromDb(errorType);
        List<String> emailList = new ArrayList<>();
        for (int i = 0; i < groupDtos.size(); i++) {
            String url = "http://localhost:9090/api/v1/users/" + groupDtos.get(i).toString();
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<UserDTO>() {}
            );
            emailList.add(response.getBody().getUserDetails().getEmail());
        }
        return emailList;
    }

    public List<GroupDto> getUsersUuidFromDb(Long errorTypeId) {
        NotificationType type = new NotificationType();
        type.setId(errorTypeId);
        return groupsRepository.findAllByTypeId(type).stream().map(converter::entityToDto).collect(Collectors.toList());
    }
}