package org.net.webcoreservice.dto;

import lombok.Data;

@Data
public class TrackedEquipmentDto {
    private Long id;
    private String uuid;
    private String title;
    private String ip;
    private String onlineStatus;
    private String mac;
}
