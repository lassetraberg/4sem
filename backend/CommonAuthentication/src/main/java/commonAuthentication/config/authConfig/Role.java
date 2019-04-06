package commonAuthentication.config.authConfig;

public enum Role implements io.javalin.security.Role {
    ANYONE, AUTHENTICATED, ADMIN
}
