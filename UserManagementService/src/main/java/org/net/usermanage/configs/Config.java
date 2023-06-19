package org.net.usermanage.configs;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.net.usermanage.properties.KeycloakManageProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        KeycloakManageProperty.class
)
@RequiredArgsConstructor
public class Config {

  private final KeycloakManageProperty property;


  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .serverUrl(property.getServerUrl())
        .realm(property.getMasterRealm())
        .username(property.getUsername())
        .password(property.getPassword())
        .clientId(property.getClientId())
        .grantType(OAuth2Constants.PASSWORD)
        .build();
  }
}
