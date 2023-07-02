package users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class UserDetailsDTO {
    private String givenName;
    private String familyName;
    private String email;
    private Map<String, Object> attributes;
}
