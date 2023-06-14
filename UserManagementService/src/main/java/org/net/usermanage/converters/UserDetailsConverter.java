package org.net.usermanage.converters;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import users.data.UserDetails;

import java.util.*;

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
}
