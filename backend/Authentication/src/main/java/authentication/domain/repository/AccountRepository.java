package authentication.domain.repository;

import authentication.domain.Account;
import common.data.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class AccountRepository extends DatabaseConnection {
    public Long createUser(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?) RETURNING account_id;";
        AtomicLong createdId = new AtomicLong(-1);

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                createdId.set(result.getLong("account_id"));
            }
        });

        return createdId.get();
    }

    public Account findByUsername(String username) {
        String sql = "SELECT account_id, username, password, created, last_login, login_attempts, last_login_attempt FROM account WHERE username = ?;";
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
        return new Account(
                rs.getLong("account_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getTimestamp("created").toInstant(),
                lastLogin,
                rs.getInt("login_attempts"),
                lastLoginAttempt
        );
    }
}
