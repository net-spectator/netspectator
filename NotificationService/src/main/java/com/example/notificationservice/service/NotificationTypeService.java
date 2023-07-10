package com.example.notificationservice.service;

import com.example.notificationservice.Repository.NotificationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTypeService {

    private final NotificationTypeRepository typeRepository;

    public String getTypeById(Long typeId) {
        return typeRepository.findById(typeId).get().getTypeTitle();
    }
}
