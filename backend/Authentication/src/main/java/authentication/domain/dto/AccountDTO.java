package authentication.domain.dto;

import commonAuthentication.config.authConfig.Role;

public class AccountDTO {
    private String username;
    private String password;
    private Role role = Role.AUTHENTICATED;

    public AccountDTO(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AccountDTO() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
