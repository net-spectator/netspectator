package org.net.usermanage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.net.usermanage.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTitle(String title);
}
