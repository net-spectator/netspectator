package ru.larionov.inventoryservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@Setter
@Getter
public class PairRegistrationNumbers {

    private Long objectId;
    private Long placeId;
}
