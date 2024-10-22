package org.net.users.converters;

import org.net.users.entities.Role;
import org.springframework.stereotype.Component;
import users.dtos.RoleDTO;

@Component
public class RoleConverter {
    public RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getTitle());
    }
}
