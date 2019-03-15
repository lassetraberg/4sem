package authentication;

import authentication.config.authConfig.AuthConfig;
import authentication.domain.repository.AccountRepository;
import authentication.domain.service.AccountService;
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

import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthenticationProvider implements IRouterService, IAccessManagerService {
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
    public void configure(Handler handler, Context ctx, Set<Role> permittedRoles) throws Exception {
        authConfig.configure(handler, ctx, permittedRoles);
    }
}
