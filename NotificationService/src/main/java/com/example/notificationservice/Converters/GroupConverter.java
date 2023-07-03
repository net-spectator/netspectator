package com.example.notificationservice.Converters;

import com.example.notificationservice.Dto.GroupDto;
import com.example.notificationservice.entity.Groups;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter {

    public GroupDto entityToDto(Groups entity) {
        GroupDto dto = new GroupDto();
        dto.setUuid(entity.getUserUuid());
        return dto;
    }
}
