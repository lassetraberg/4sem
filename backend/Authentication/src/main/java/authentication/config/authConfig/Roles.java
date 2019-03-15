package authentication.config.authConfig;

import io.javalin.security.Role;

public enum Roles implements Role {
    ANYONE, AUTHENTICATED
}
