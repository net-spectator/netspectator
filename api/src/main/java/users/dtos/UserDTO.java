package users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String username;
    private Collection<RoleDTO> roles;
    private UserDetailsDTO userDetails;
}
