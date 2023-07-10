package converters;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.net.usermanage.entities.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import users.dtos.UserDetailsDTO;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsConverter {

    private JSONObject userAttributes;
    private String email;
    public UserDetails authToUserDetails(BearerTokenAuthentication auth) {
        userAttributes = null;
        email = "";
        Optional.ofNullable(auth.getTokenAttributes().get("email")).ifPresent(o ->
                email = o.toString()
        );
        Optional.ofNullable(auth.getTokenAttributes().get("user")).ifPresent(o ->
            userAttributes = (JSONObject) o
        );
        return new UserDetails(
                auth.getTokenAttributes().get("given_name").toString(),
                auth.getTokenAttributes().get("family_name").toString(),
                email,
                userAttributes
        );
    }

    public UserDetailsDTO toDTO(UserDetails userDetails) {
        return new UserDetailsDTO(
                userDetails.getGivenName(),
                userDetails.getFamilyName(),
                userDetails.getEmail(),
                userDetails.getAttributes()
        );
    }
}
