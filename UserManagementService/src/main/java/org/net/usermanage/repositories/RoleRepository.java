package org.net.usermanage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import users.data.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByTitle(String title);
}
