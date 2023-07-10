package users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String username;
    private Collection<RoleDTO> roles;
    private UserDetailsDTO userDetails;
}
