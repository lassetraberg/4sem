package commonAuthentication.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import common.config.Config;
import commonAuthentication.config.authConfig.Role;

import java.time.Instant;

public class Account {
    private Long id;
    private String username;

    @JsonIgnore
    private String password;
    private String token;
    private Instant created;
    private Instant lastLogin;
    private int loginAttempts;
    private Instant lastLoginAttempt;
    private Role role;
    private Instant tokenExpiresAt;

    public Account(Long id, String username, String password, Instant created, Instant lastLogin, int loginAttempts, Instant lastLoginAttempt, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created = created;
        this.lastLogin = lastLogin;
        this.loginAttempts = loginAttempts;
        this.lastLoginAttempt = lastLoginAttempt;
        this.role = role;
    }

    @JsonProperty("locked")
    public boolean isLocked() {
        return loginAttempts >= Integer.parseInt(Config.getInstance().getProperty("auth.maxLoginAttempts"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Instant getLastLoginAttempt() {
        return lastLoginAttempt;
    }

    public void setLastLoginAttempt(Instant lastLoginAttempt) {
        this.lastLoginAttempt = lastLoginAttempt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(Instant tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }
}
