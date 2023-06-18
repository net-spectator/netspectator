package users.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;
import users.converters.HashMapConverter;

import javax.persistence.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "given_name")
    private String givenName;
    @Column(name = "family_name")
    private String familyName;
    @Column(name = "email")
    private String email;
    @Column(name = "attributes")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> attributes;

    public UserDetails(final String givenName, final String familyName, final String email, final JSONObject attributes) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.attributes = attributes;
    }
}
