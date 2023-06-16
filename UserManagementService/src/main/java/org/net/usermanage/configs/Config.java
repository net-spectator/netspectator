package org.net.usermanage.configs;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  //TODO вынести в конфиг
  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .serverUrl("http://localhost:9500/")
        .realm("master")
        .username("ns-reader")
        .password("2311")
        .clientId("admin-cli")
        .grantType(OAuth2Constants.PASSWORD)
        .build();
  }
}
