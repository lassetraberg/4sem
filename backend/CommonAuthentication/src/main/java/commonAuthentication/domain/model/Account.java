package commonAuthentication.domain.model;

import java.time.Instant;

public class Account {
    private Long id;
    private String username;
    private String password;
    private String token;
    private Instant created;
    private Instant last_login;
    private int loginAttempts;
    private Instant lastLoginAttempt;

    public Account(Long id, String username, String password, Instant created, Instant last_login, int loginAttempts, Instant lastLoginAttempt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created = created;
        this.last_login = last_login;
        this.loginAttempts = loginAttempts;
        this.lastLoginAttempt = lastLoginAttempt;
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

    public Instant getLast_login() {
        return last_login;
    }

    public void setLast_login(Instant last_login) {
        this.last_login = last_login;
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
}
