package org.net.usermanage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import reactor.core.publisher.Mono;

public class KeycloakReactiveTokenInstrospector implements ReactiveOpaqueTokenIntrospector {

    private final ReactiveOpaqueTokenIntrospector delegate;

    public KeycloakReactiveTokenInstrospector(ReactiveOpaqueTokenIntrospector delegate) {
        this.delegate = delegate;
    }

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {

        return delegate.introspect(token)
                .map( this::mapPrincipal);
    }

    protected OAuth2AuthenticatedPrincipal mapPrincipal(OAuth2AuthenticatedPrincipal principal) {

        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(),
                principal.getAttributes(),
                extractAuthorities(principal));
    }

    protected Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {

        Map<String,List<String>> realm_access = principal.getAttribute("realm_access");
        List<String> roles = realm_access.getOrDefault("roles", Collections.emptyList());
        List<GrantedAuthority> rolesAuthorities = roles.stream()
                .filter(r -> r.startsWith("ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return rolesAuthorities;
    }

}
