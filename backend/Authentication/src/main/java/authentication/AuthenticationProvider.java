package authentication;

import authentication.config.authConfig.AuthConfig;
import authentication.domain.repository.AccountRepository;
import authentication.domain.repository.IAccountRepository;
import authentication.domain.service.AccountService;
import authentication.domain.service.IAccountService;
import authentication.util.Hasher;
import authentication.util.IHasher;
import authentication.util.JwtProvider;
import authentication.web.AccountController;
import common.spi.IAccessManagerService;
import common.spi.IRouterService;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.Role;

import java.util.Collections;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthenticationProvider implements IRouterService, IAccessManagerService {
    private JwtProvider jwtProvider = new JwtProvider();
    private AuthConfig authConfig = new AuthConfig(jwtProvider);
    private IHasher hasher = new Hasher();

    private IAccountRepository accountRepository = new AccountRepository();
    private IAccountService accountService = new AccountService(accountRepository, jwtProvider, hasher);
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
    public void configure(Handler handler, Context ctx, Set<Role> permittedRoles) throws Exception {
        authConfig.configure(handler, ctx, permittedRoles);
    }
}
