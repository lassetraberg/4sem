package authentication.domain.service;

import authentication.util.IHasher;
import authentication.util.JwtProvider;
import common.web.exceptions.ValidationException;
import commonAuthentication.config.authConfig.Roles;
import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import io.javalin.UnauthorizedResponse;

public class AccountService implements IAccountService {
    private IAccountRepository accountRepository;
    private JwtProvider jwtProvider;
    private IHasher hasher;

    public AccountService(IAccountRepository accountRepository, JwtProvider jwtProvider, IHasher hasher) {
        this.accountRepository = accountRepository;
        this.jwtProvider = jwtProvider;
        this.hasher = hasher;
    }

    public Account create(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null) {
            throw new ValidationException("Username already registered");
        }
        if (account.getPassword().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
        if (account.getUsername().length() < 2) {
            throw new ValidationException("Username must be at least 2 characters");
        }

        String hashedPassword = hasher.hashPassword(account.getPassword());
        account.setPassword(hashedPassword);
        account.setToken(generateJwtToken(account));
        accountRepository.createUser(account);
        return account;
    }

    public Account authenticate(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount == null) {
            throw new UnauthorizedResponse("Invalid username or password!");
        }

        if (!hasher.isPasswordCorrect(account.getPassword(), existingAccount.getPassword())) {
            throw new UnauthorizedResponse("Invalid username or password!");
        }

        existingAccount.setToken(generateJwtToken(account));
        return existingAccount;
    }

    private String generateJwtToken(Account account) {
        return jwtProvider.createJWT(account, Roles.AUTHENTICATED);
    }
}
