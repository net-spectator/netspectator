package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.Sensors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorsRepository extends JpaRepository<Sensors, Long> {
}
