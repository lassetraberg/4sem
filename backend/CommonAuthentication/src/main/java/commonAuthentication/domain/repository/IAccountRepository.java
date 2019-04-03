package commonAuthentication.domain.repository;


import commonAuthentication.domain.model.Account;

public interface IAccountRepository {
    /**
     * Create a new account, returning the id of the newly created account.
     *
     * @param account Account object containing username and password
     * @return id of newly created account. Returns -1 if the account was not created.
     */
    Long createUser(Account account);

    /**
     * Find an account, based on a username.
     *
     * @param username A username
     * @return account belonging to the username. Returns null of no account was found
     */
    Account findByUsername(String username);
}
