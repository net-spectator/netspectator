package org.net.webcoreservice.repository;

import org.net.webcoreservice.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device,Long> {
}
