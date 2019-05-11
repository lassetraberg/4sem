package grp4.commonAuthentication.domain.repository;


import grp4.commonAuthentication.domain.model.Account;

import java.time.Instant;
import java.util.List;

public interface IAccountRepository {
    /**
     * Create a new account, returning the id of the newly created account.
     *
     * @param account Account object containing username and password
     * @return id of newly created account. Returns -1 if the account was not created.
     */
    Account createUser(Account account);

    /**
     * Find an account, based on a username.
     *
     * @param username A username
     * @return account belonging to the username. Returns null of no account was found
     */
    Account findByUsername(String username);

    /**
     * Find an account, based on account id.
     *
     * @param id account id
     * @return account belonging with that account id. Returns null of no account was found
     */
    Account findById(Long id);

    /**
     * Updates the last time, the account was logged in,
     *
     * @param account an account
     * @return The last logged in time
     */
    Instant updateLastLogin(Account account);

    /**
     * Update the last login attempt value.
     *
     * @param account an account
     * @return how many login attempts there are now
     */
    int updateLoginAttempts(Account account);

    /**
     * Unlock an account
     *
     * @param account an account
     * @return true if it was unlocked successfully
     */
    boolean unlockAccount(Account account);

    /**
     * Find all accounts in the database
     *
     * @return list of accounts
     */
    List<Account> findAll();
}
