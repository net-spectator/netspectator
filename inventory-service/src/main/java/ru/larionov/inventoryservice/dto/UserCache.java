package ru.larionov.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import users.dtos.UserDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCache {
    private Long timeToLive;
    private UserDTO userDTO;
}
