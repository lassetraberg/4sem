package authentication.domain.service;

import commonAuthentication.domain.model.Account;

public interface IAccountService {
    /**
     * Create an account, returning the newly created account.
     *
     * @param account an account
     * @return the newly created account
     */
    Account create(Account account);

    /**
     * Authenticate an account, providing the JWT token.
     *
     * @param account an account
     * @return an account object, with the token value set
     */
    Account authenticate(Account account);
}
