package com.example.notificationservice.service;

import com.example.notificationservice.Repository.NotificationTypeRepository;
import com.example.notificationservice.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationTypeService {

    private final NotificationTypeRepository typeRepository;

    public String getTypeById(Long errTypeId) {
        return typeRepository.findById(errTypeId).get().getErrorType();
    }
}
