package org.net.usermanage.converters;

import lombok.RequiredArgsConstructor;
import org.net.usermanage.entities.User;
import org.springframework.stereotype.Component;
import users.dtos.UserDTO;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final RoleConverter roleConverter;
    private final UserDetailsConverter userDetailsConverter;

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getUsername(),
                user.getRoles().stream().map(r -> roleConverter.toDTO(r)).collect(Collectors.toList()),
                userDetailsConverter.toDTO(user.getUserDetails())
        );
    }
}
