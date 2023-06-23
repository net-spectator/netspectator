package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.TrackedEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackedEquipmentRepository extends JpaRepository<TrackedEquipment, Long> {

    @Query("SELECT tr FROM TrackedEquipment tr WHERE tr.blackList = 1")
    List<TrackedEquipment> showBlackList();
}
