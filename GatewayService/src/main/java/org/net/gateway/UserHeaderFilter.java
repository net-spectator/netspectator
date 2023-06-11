package org.net.gateway;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserHeaderFilter implements GlobalFilter {

    @Value("${introspect_field}")
    private String introspect;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .flatMap(c -> {
                    OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal = mapPrincipal((OAuth2AuthenticatedPrincipal) c.getAuthentication().getPrincipal());
                    String sub = oAuth2AuthenticatedPrincipal.getAttribute(introspect);
                    if (Strings.isNullOrEmpty(sub)) {
                        return Mono.error(
                                new AccessDeniedException("Токен не прошел проверку.")
                        );
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("x-introspect", sub).build();

                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    protected OAuth2AuthenticatedPrincipal mapPrincipal(OAuth2AuthenticatedPrincipal principal) {

        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(),
                principal.getAttributes(),
                extractAuthorities(principal));
    }

    protected Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {

        Map<String, List<String>> realm_access = principal.getAttribute("realm_access");
        List<String> roles = realm_access.getOrDefault("roles", Collections.emptyList());
        List<GrantedAuthority> rolesAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Set<GrantedAuthority> allAuthorities = new HashSet<>();
        allAuthorities.addAll(principal.getAuthorities());
        allAuthorities.addAll(rolesAuthorities);

        return allAuthorities;
    }
}