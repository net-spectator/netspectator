package org.net.usermanage.services;

import lombok.RequiredArgsConstructor;
import org.net.users.entities.Role;
import org.net.users.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(String title) {
        return roleRepository.findByTitle(title).get();
    }
}
