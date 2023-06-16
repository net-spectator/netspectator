package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.Routers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutersRepository extends JpaRepository<Routers, Long> {
}
