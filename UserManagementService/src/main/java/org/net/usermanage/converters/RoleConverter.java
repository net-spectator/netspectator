package org.net.usermanage.converters;

import org.net.usermanage.entities.Role;
import org.springframework.stereotype.Component;
import users.dtos.RoleDTO;

@Component
public class RoleConverter {
    public RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getTitle());
    }
}
