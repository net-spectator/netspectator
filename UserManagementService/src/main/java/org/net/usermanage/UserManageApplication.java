package org.net.usermanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.server.resource.introspection.NimbusReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;

@SpringBootApplication
@EnableWebFluxSecurity
@EnableJpaRepositories(basePackages = {"org.net.users.repositories"})
@EntityScan(basePackages = {"org.net.users.entities"})
@ComponentScan(basePackages = {"org.net.users.converters", "org.net.usermanage"})
public class UserManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserManageApplication.class);
    }

    @Bean
    public ReactiveOpaqueTokenIntrospector keycloakIntrospector(OAuth2ResourceServerProperties props) {

        NimbusReactiveOpaqueTokenIntrospector delegate = new NimbusReactiveOpaqueTokenIntrospector(
                props.getOpaquetoken().getIntrospectionUri(),
                props.getOpaquetoken().getClientId(),
                props.getOpaquetoken().getClientSecret());

        return new KeycloakReactiveTokenInstrospector(delegate);
    }
}
