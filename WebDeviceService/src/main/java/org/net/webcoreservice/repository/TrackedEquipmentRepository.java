package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.TrackedEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackedEquipmentRepository extends JpaRepository<TrackedEquipment, Long> {
}
