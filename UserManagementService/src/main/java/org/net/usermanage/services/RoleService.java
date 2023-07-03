package org.net.usermanage.services;

import lombok.RequiredArgsConstructor;
import org.net.usermanage.repositories.RoleRepository;
import org.springframework.stereotype.Service;
import org.net.usermanage.entities.Role;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(String title) {
        return roleRepository.findByTitle(title).get();
    }
}
