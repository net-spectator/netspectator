package org.net.usermanage.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.net.usermanage.converters.UserConverter;
import org.net.usermanage.converters.UserDetailsConverter;
import org.net.usermanage.repositories.UserRepository;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.net.usermanage.entities.Role;
import org.net.usermanage.entities.User;
import org.net.usermanage.entities.UserDetails;
import users.dtos.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserActualizeService {

    private final UserRepository userRepository;
    private final UserDetailsConverter userDetailsConverter;
    private final UserConverter userConverter;

    private final RoleService roleService;

    public Optional<User> getUser(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(u -> userConverter.toDTO(u)).collect(Collectors.toList());
    }

    public void checkUser(BearerTokenAuthentication auth) {
        UserDetails userDetails = userDetailsConverter.authToUserDetails(auth);
        Collection<Role> roles = auth.getAuthorities().stream().map(
                e -> roleService.getRole(e.toString())
        ).collect(Collectors.toList());
        User user = new User(UUID.fromString(
                auth.getTokenAttributes().get("sub").toString()),
                auth.getTokenAttributes().get("preferred_username").toString(),
                roles,
                userDetails);
        Optional<User> storedUser = getUser(user.getUuid());
        if (!storedUser.isPresent()) {
            saveUser(user);
        } else {
            user.setId(storedUser.get().getId());
            user.getUserDetails().setId(storedUser.get().getUserDetails().getId());
            Gson gson = new Gson();
            if (!gson.toJson(storedUser.get()).equals(gson.toJson(user)))
                saveUser(user);
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
