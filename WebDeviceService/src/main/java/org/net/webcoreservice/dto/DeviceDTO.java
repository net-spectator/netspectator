package org.net.webcoreservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class DeviceDTO {
    private Long id;
    private String title;
    private String uuid;
    private boolean status;
}
