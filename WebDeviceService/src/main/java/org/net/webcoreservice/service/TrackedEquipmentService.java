package org.net.webcoreservice.service;

import entities.devices.ClientHardwareInfo;
import entities.nodes.DetectedNode;
import lombok.RequiredArgsConstructor;
import org.net.webcoreservice.Enum.BlackListStatus;
import org.net.webcoreservice.dto.TrackedEquipmentDto;
import org.net.webcoreservice.entities.TrackedEquipment;
import org.net.webcoreservice.exeptions.ResourceNotFoundException;
import org.net.webcoreservice.repository.TrackedEquipmentRepository;
import org.springframework.stereotype.Service;
import services.ClientListenersDataBus;
import services.NodeListener;

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

    public List<TrackedEquipment> showBlackList() {
        return trackedEquipmentRepository.showBlackList();
    }

    public void addToBlackList(Long id) {
        if (!isExist(id)) {
            throw new ResourceNotFoundException("Оборудование с id" + id + " не найдено.");
        }
        blackListOperation(id, BlackListStatus.DISABLE.getStatus());
        disconnect(id);
    }

    public void removeFromBlackList(Long id) {
        if (!isExist(id)) {
            throw new ResourceNotFoundException("Устройства с id" + id + " не найдено.");
        }
        blackListOperation(id, BlackListStatus.ENABLE.getStatus());
    }

    public void disconnect(Long id) {
        ClientListenersDataBus.disconnectClient(id);
    }

    public ClientHardwareInfo getEquipmentHardwareInfo(Long id) {
        return ClientListenersDataBus.getClientHardwareInfo(id);
    }

    public void scanNetwork(String ip) {
        NodeListener.nodeBroadcastSearch(ip);
    }

    public List<DetectedNode> getNodes(){
        return NodeListener.getDetectedNodes();
    }

    public void addNodesByIndex(int index) {
        NodeListener.addNodeForTracking(index);
    }

    public void addNodesWithChangeName(int index, String newName) {
        NodeListener.addNodeForTracking(index, newName);
    }

    public void removeNodeFromTrEq(int id) {
        NodeListener.removeNodeFromTrackingById(id);
    }

    public TrackedEquipment createNewTrackedEquipment(TrackedEquipmentDto trackedEquipmentDto) {
        TrackedEquipment trackedEquipment = new TrackedEquipment();
        trackedEquipment.setEquipmentUuid(trackedEquipmentDto.getUuid());
        trackedEquipment.setEquipmentTitle(trackedEquipmentDto.getTitle());
        trackedEquipment.setEquipmentIpAddress(trackedEquipmentDto.getIp());
        trackedEquipment.setEquipmentOnlineStatus(trackedEquipmentDto.getOnlineStatus());
        trackedEquipment.setEquipmentMacAddress(trackedEquipmentDto.getMac());
        return trackedEquipmentRepository.save(trackedEquipment);
    }

    public void blackListOperation(Long id, int blackListStatus) {
        TrackedEquipment trackedEquipment = findById(id).get();
        trackedEquipment.setBlackList(blackListStatus);
        trackedEquipmentRepository.save(trackedEquipment);
    }

    public boolean isExist(Long id) {
        Optional<TrackedEquipment> entity = findById(id);
        if (!entity.isPresent()) {
            return false;
        }
        return true;
    }
}
