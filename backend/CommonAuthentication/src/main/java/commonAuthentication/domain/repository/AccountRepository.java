package commonAuthentication.domain.repository;

import common.data.database.DatabaseConnection;
import commonAuthentication.config.authConfig.Role;
import commonAuthentication.domain.model.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AccountRepository extends DatabaseConnection implements IAccountRepository {
    public Long createUser(Account account) {
        String sql = "INSERT INTO account (username, password, role) VALUES (?, ?, ?) RETURNING account_id;";
        AtomicLong createdId = new AtomicLong(-1);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getRole().name());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                createdId.set(result.getLong("account_id"));
            }
        });

        return createdId.get();
    }

    public Account findByUsername(String username) {
        String sql = "SELECT account_id, username, password, created, last_login, login_attempts, last_login_attempt, role FROM account WHERE username = ?;";
        AtomicReference<Account> user = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                user.set(fromResultSet(result));
            }
        });

        return user.get();
    }

    private Account fromResultSet(ResultSet rs) throws SQLException {
        Instant lastLogin = null;
        Instant lastLoginAttempt = null;
        if (rs.getTimestamp("last_login") != null) {
            lastLogin = rs.getTimestamp("last_login").toInstant();
        }
        if (rs.getTimestamp("last_login_attempt") != null) {
            lastLoginAttempt = rs.getTimestamp("last_login_attempt").toInstant();
        }
        String roleStr = rs.getString("role");
        return new Account(
                rs.getLong("account_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getTimestamp("created").toInstant(),
                lastLogin,
                rs.getInt("login_attempts"),
                lastLoginAttempt,
                Role.valueOf(roleStr)
        );
    }
}
