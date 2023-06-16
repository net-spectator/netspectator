package org.net.usermanage.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import users.data.Role;
import users.data.User;
import users.data.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class KeycloakService {

  private final Keycloak keycloak;
  private final RoleService roleService;
  private final UserActualizeService userActualizeService;

  //TODO вынести в конфиг
  private final String REALM = "netspectator";
  private Map<String, Object> attributes;


  /*
  * TODO
  *  Проблема: При изменении аттрибутов в админке keycloak из UserRepresentations пропадают поля firstName и lastName.
  *  После сохранения на вкладке Details в админке поля возвращают нужные значения.
  *  При REST запросе, такой проблемы нет. Проблема явно на уровне Keycloak Admin Client.
  */
  public List<UserRepresentation> getUsers() {
    return keycloak.realm(REALM).users().list();
  }

  public List<Role> getMappedRoles(String id) {
    List<RoleRepresentation> roleRepresentations = keycloak
            .realm(REALM)
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

  @Scheduled(fixedDelay = 30000) // TODO Вынести значение fixedDelay в конфиг
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
