package grp4.commonAuthentication.domain.repository;

import grp4.common.data.database.DatabaseConnection;
import grp4.commonAuthentication.config.authConfig.Role;
import grp4.commonAuthentication.domain.model.Account;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AccountRepository extends DatabaseConnection implements IAccountRepository {
    public Account createUser(Account account) {
        String sql = "INSERT INTO account (username, password, role) VALUES (?, ?, ?) RETURNING account_id, username, password, created, last_login, login_attempts, last_login_attempt, role;";
        AtomicReference<Account> createdAccount = new AtomicReference<>();

        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getRole().name());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                createdAccount.set(fromResultSet(result));
            }
        });

        return createdAccount.get();
    }

    @Override
    public Account findByUsername(String username) {
        return findBy("username", username);
    }

    @Override
    public Account findById(Long id) {
        return findBy("account_id", id);
    }

    @Override
    public Instant updateLastLogin(Account account) {
        String sql = "UPDATE account SET last_login = now(), login_attempts = 0, last_login_attempt = null WHERE account_id = ? RETURNING last_login";
        AtomicReference<Instant> instantAtomicReference = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, account.getId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                instantAtomicReference.set(rs.getTimestamp("last_login").toInstant());
            }
        });

        return instantAtomicReference.get();
    }

    @Override
    public int updateLoginAttempts(Account account) {
        String sql = "UPDATE account SET login_attempts = login_attempts + 1, last_login_attempt = now() WHERE account_id = ? RETURNING login_attempts";
        AtomicReference<Integer> loginAttemptsReference = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, account.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loginAttemptsReference.set(rs.getInt("login_attempts"));
            }
        });

        return loginAttemptsReference.get();
    }

    @Override
    public boolean unlockAccount(Account account) {
        String sql = "UPDATE account SET login_attempts = 0, last_login_attempt = null WHERE account_id = ?";
        AtomicBoolean success = new AtomicBoolean(false);
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, account.getId());

            int res = stmt.executeUpdate();
            if (res != 0) {
                success.set(true);
            }
        });

        return success.get();
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT account_id, username, password, created, last_login, login_attempts, last_login_attempt, role FROM account";
        List<Account> accountList = new ArrayList<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                accountList.add(fromResultSet(rs));
            }
        });

        return accountList;
    }

    private Account findBy(String field, Object value) {
        String sql = "SELECT account_id, username, password, created, last_login, login_attempts, last_login_attempt, role FROM account WHERE " + field + " = ?;";
        AtomicReference<Account> user = new AtomicReference<>();
        this.executeQuery(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            switch (value.getClass().getSimpleName()) {
                case "String":
                    stmt.setString(1, (String) value);
                    break;
                case "Long":
                    stmt.setLong(1, (Long) value);
                    break;
                default:
                    throw new RuntimeException(String.format("%s is not a supported type of value", value.getClass().getSimpleName()));
            }

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
