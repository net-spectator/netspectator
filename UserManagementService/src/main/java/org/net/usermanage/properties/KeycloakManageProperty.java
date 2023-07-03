package org.net.usermanage.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.management.props")
@Data
public class KeycloakManageProperty {

    private String serverUrl;
    private String masterRealm;
    private String username;
    private String password;
    private String clientId;

}
