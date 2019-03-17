package authentication.domain.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import authentication.domain.Account;
import authentication.domain.repository.AccountRepository;
import authentication.util.JwtProvider;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.HttpResponseException;
import io.javalin.UnauthorizedResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class AccountService {
    private AccountRepository accountRepository;
    private JwtProvider jwtProvider;

    public AccountService(AccountRepository accountRepository, JwtProvider jwtProvider) {
        this.accountRepository = accountRepository;
        this.jwtProvider = jwtProvider;
    }

    public Account create(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null) {
            Map<String, String> details = Collections.singletonMap("Error", "Username already registered");
            throw new HttpResponseException(HttpStatus.BAD_REQUEST_400,
                    "Username already registered", details);
        } else {
            String hashedPassword = generateHashedPassword(account.getPassword());
            account.setPassword(hashedPassword);
            account.setToken(generateJwtToken(account));
            accountRepository.createUser(account);
            return account;
        }
    }

    public Account authenticate(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount == null) {
            throw new UnauthorizedResponse("Invalid username or password!");
        }

        if (isPasswordCorrect(account.getPassword(), existingAccount.getPassword())) {
            existingAccount.setToken(generateJwtToken(account));
            return existingAccount;
        } else {
            throw new UnauthorizedResponse("Invalid username or password!");
        }
    }

    private String generateJwtToken(Account account) {
        return jwtProvider.createJWT(account, Roles.AUTHENTICATED);
    }

    private String generateHashedPassword(String plaintextPassword) {
        return BCrypt.withDefaults().hashToString(12, plaintextPassword.toCharArray());
    }

    private boolean isPasswordCorrect(String plaintextPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plaintextPassword.toCharArray(), hashedPassword).verified;
    }
}
