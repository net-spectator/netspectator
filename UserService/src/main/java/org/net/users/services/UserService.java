package org.net.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.net.users.converters.UserConverter;
import org.net.users.entities.User;
import org.net.users.repositories.UserRepository;
import org.springframework.stereotype.Service;
import users.dtos.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;


    public Optional<User> getUser(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(u -> userConverter.toDTO(u)).collect(Collectors.toList());
    }
}
