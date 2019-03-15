package authentication;

import authentication.config.authConfig.AuthConfig;
import authentication.domain.repository.AccountRepository;
import authentication.domain.service.AccountService;
import authentication.util.JwtProvider;
import authentication.web.AccountController;
import common.spi.IConfigurationService;
import common.spi.IRouterService;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import java.util.Collections;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationProvider implements IRouterService, IConfigurationService {
    private JwtProvider jwtProvider = new JwtProvider();
    private AuthConfig authConfig = new AuthConfig(jwtProvider);

    private AccountRepository accountRepository = new AccountRepository();
    private AccountService accountService = new AccountService(accountRepository, jwtProvider);
    private AccountController accountController = new AccountController(accountService);

    @Override
    public EndpointGroup getRoutes() {
        return (() -> {
            path("/accounts", () -> {
                post(accountController::register, Collections.singleton(Roles.ANYONE));
                post("/login", accountController::login, Collections.singleton(Roles.ANYONE));
            });
        });
    }

    @Override
    public void configure(Javalin app) {
        authConfig.configure(app);
    }
}
