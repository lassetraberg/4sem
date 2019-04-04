package common.spi;

import io.javalin.security.Role;

import java.util.Set;

public interface IWebSocketAuthenticationService {
    boolean doesUserHaveRole(Set<Role> permittedRoles, String authMsg);

    String getUsername(String authMsg);
}
