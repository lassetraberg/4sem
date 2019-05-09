package authentication.domain.service;

import commonAuthentication.domain.model.Account;

import java.util.List;

public interface IAccountService {
    /**
     * Create an account, returning the newly created account.
     *
     * @param account  an account
     * @param sourceIp the ip that made the creation request
     * @return the newly created account
     */
    Account create(Account account, String sourceIp);

    /**
     * Authenticate an account, providing the JWT token.
     *
     * @param account an account
     * @return an account object, with the token value set
     */
    Account authenticate(Account account);

    /**
     * Unlock an account
     *
     * @param accountId id of account
     * @return true if the account was unlocked successfully
     */
    boolean unlockAccount(Long accountId);

    /**
     * Get all accounts in the system
     *
     * @return List of accounts
     */
    List<Account> getAllAccounts();
}
