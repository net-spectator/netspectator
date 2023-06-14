package org.net.usermanage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import users.data.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);
}
