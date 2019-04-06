package authentication.domain.service;

import authentication.util.IHasher;
import authentication.util.JwtProvider;
import common.web.exceptions.ValidationException;
import commonAuthentication.config.authConfig.Role;
import commonAuthentication.domain.model.Account;
import commonAuthentication.domain.repository.IAccountRepository;
import io.javalin.NotFoundResponse;
import io.javalin.UnauthorizedResponse;

import java.time.Instant;

public class AccountService implements IAccountService {
    private IAccountRepository accountRepository;
    private JwtProvider jwtProvider;
    private IHasher hasher;

    private int maxLoginAttempts = 6;

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
        if (account.getRole() == Role.ANYONE) {
            throw new ValidationException("You cannot create a user with that role");
        }

        String hashedPassword = hasher.hashPassword(account.getPassword());
        account.setPassword(hashedPassword);
        Account createdAccount = accountRepository.createUser(account);
        createdAccount.setToken(generateJwtToken(account));
        return createdAccount;
    }

    public Account authenticate(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount == null) {
            throw new UnauthorizedResponse("Invalid username or password!");
        }

        if (existingAccount.getLoginAttempts() >= maxLoginAttempts) {
            throw new UnauthorizedResponse("This account has been locked. Contact an administrator to unlock it.");
        }

        if (!hasher.isPasswordCorrect(account.getPassword(), existingAccount.getPassword())) {
            Integer loginAttempts = accountRepository.updateLoginAttempts(existingAccount);
            throw new UnauthorizedResponse("Invalid username or password!");
        }

        Instant lastLogin = accountRepository.updateLastLogin(existingAccount);
        existingAccount.setLastLogin(lastLogin);
        existingAccount.setToken(generateJwtToken(existingAccount));
        return existingAccount;
    }

    @Override
    public boolean unlockAccount(Long accountId) {
        Account lockedAccount = accountRepository.findById(accountId);
        if (lockedAccount == null) {
            throw new NotFoundResponse("Account not found");
        }

        return accountRepository.unlockAccount(lockedAccount);
    }

    private String generateJwtToken(Account account) {
        return jwtProvider.createJWT(account, account.getRole());
    }
}
