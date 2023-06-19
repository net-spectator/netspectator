package org.net.usermanage.controllers;

import lombok.RequiredArgsConstructor;
import org.net.usermanage.services.UserActualizeService;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class UserActualizeController {

    private final UserActualizeService userActualizeService;

    @GetMapping
    public void checkUser(BearerTokenAuthentication auth) {
        userActualizeService.checkUser(auth);
    }

}
