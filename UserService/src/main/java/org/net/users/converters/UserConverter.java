package org.net.users.converters;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.net.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import users.dtos.UserDTO;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class UserConverter {

    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private UserDetailsConverter userDetailsConverter;

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getUsername(),
                user.getRoles().stream().map(r -> roleConverter.toDTO(r)).collect(Collectors.toList()),
                userDetailsConverter.toDTO(user.getUserDetails())
        );
    }
}
