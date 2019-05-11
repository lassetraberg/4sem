package grp4.commonAuthentication.config.authConfig;

public enum Role implements io.javalin.security.Role {
    ANYONE, AUTHENTICATED, ADMIN;

    public boolean equals(String roleStr) {
        return this.name().equalsIgnoreCase(roleStr);
    }
}
