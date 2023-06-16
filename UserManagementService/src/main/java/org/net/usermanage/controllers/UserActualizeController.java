package org.net.usermanage.controllers;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.net.usermanage.services.KeycloakService;
import org.net.usermanage.services.UserActualizeService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import users.data.User;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class UserActualizeController {

    private final UserActualizeService userActualizeService;
//    private final KeycloakService keycloakService;

    @GetMapping
    public void checkUser(BearerTokenAuthentication auth) {
        userActualizeService.checkUser(auth);
    }

//    @GetMapping("autocheck")
//    public void autocheckUsers() {
//        keycloakService.autocheckUsers();
//    }
}
