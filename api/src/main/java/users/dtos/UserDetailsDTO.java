package users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String givenName;
    private String familyName;
    private String email;
    private Map<String, Object> attributes;
}
