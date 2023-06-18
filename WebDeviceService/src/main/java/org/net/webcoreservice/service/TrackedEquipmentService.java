package org.net.webcoreservice.service;

import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.dto.TrackedEquipmentDto;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.net.webcoreservice.repository.TrackedEquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackedEquipmentService {

    private final TrackedEquipmentRepository trackedEquipmentRepository;

    public Optional<TrackedEquipment> findById(Long id) {
        return trackedEquipmentRepository.findById(id);
    }

    public List<TrackedEquipment> findAll() {
        return trackedEquipmentRepository.findAll();
    }

    public void deleteById(Long id) {
        trackedEquipmentRepository.deleteById(id);
    }

    public void createNewTrackedEquipment(TrackedEquipmentDto trackedEquipmentDto) {
        TrackedEquipment trackedEquipment = new TrackedEquipment();
        trackedEquipment.setEquipmentUuid(trackedEquipmentDto.getUuid());
        trackedEquipment.setEquipmentTitle(trackedEquipmentDto.getTitle());
        trackedEquipment.setEquipmentIpAddress(trackedEquipmentDto.getIp());
        trackedEquipment.setEquipmentOnlineStatus(trackedEquipmentDto.getOnlineStatus());
        trackedEquipment.setEquipmentMacAddress(trackedEquipmentDto.getMac());
        trackedEquipmentRepository.save(trackedEquipment);
    }
}
