package org.net.users.converters;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.net.users.entities.UserDetails;
import org.springframework.stereotype.Component;
import users.dtos.UserDetailsDTO;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsConverter {

    private JSONObject userAttributes;
    private String email;

    public UserDetailsDTO toDTO(UserDetails userDetails) {
        return new UserDetailsDTO(
                userDetails.getGivenName(),
                userDetails.getFamilyName(),
                userDetails.getEmail(),
                userDetails.getAttributes()
        );
    }
}
