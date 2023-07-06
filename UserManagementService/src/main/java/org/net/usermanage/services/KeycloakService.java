package org.net.usermanage.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.net.usermanage.entities.Role;
import org.net.usermanage.entities.User;
import org.net.usermanage.entities.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class KeycloakService {

  private final Keycloak keycloak;
  private final RoleService roleService;
  private final UserActualizeService userActualizeService;

  @Value("${realm}")
  private String realm;

  @Value("${type-attribute}")
  private String type;

  private Map<String, Object> attributes;

  public List<UserRepresentation> getUsers() {
    return keycloak.realm(realm).users().searchByAttributes("type:" + type);
  }

  public List<Role> getMappedRoles(String id) {
    List<RoleRepresentation> roleRepresentations = keycloak
            .realm(realm)
            .users()
            .get(id)
            .roles()
            .realmLevel()
            .listEffective()
            .stream().filter(
                    r -> r.getName().startsWith("ROLE_")
            ).collect(Collectors.toList());
    List<Role> auth = roleRepresentations.stream().map(roleRepresentation ->
            roleService.getRole(roleRepresentation.getName())
    ).collect(Collectors.toList());
    return auth;
  }

  @Scheduled(fixedDelayString = "${check-delay}")
  public void autocheckUsers() {
    getUsers().stream().forEach(userRepresentation -> {
      attributes = null;
      if (userRepresentation.getAttributes() != null) {
        attributes = userRepresentation.getAttributes()
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> new AbstractMap.SimpleEntry<>(e.getKey(), v)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      }
      UserDetails userDetails = new UserDetails();
      userDetails.setGivenName(userRepresentation.getFirstName());
      userDetails.setFamilyName(userRepresentation.getLastName());
      userDetails.setEmail(userRepresentation.getEmail());
      userDetails.setAttributes(attributes);
      User user = new User(
              UUID.fromString(userRepresentation.getId()),
              userRepresentation.getUsername(),
              getMappedRoles(userRepresentation.getId()),
              userDetails
      );
      Optional<User> storedUser = userActualizeService.getUser(user.getUuid());
      if (!storedUser.isPresent()) {
        userActualizeService.saveUser(user);
      } else {
        user.setId(storedUser.get().getId());
        user.getUserDetails().setId(storedUser.get().getUserDetails().getId());
        Gson gson = new Gson();
        if (!gson.toJson(storedUser.get()).equals(gson.toJson(user)))
          userActualizeService.saveUser(user);
      }
    });
  }

}
