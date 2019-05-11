package grp4.authentication;

import grp4.authentication.config.authConfig.AuthConfig;
import grp4.authentication.config.authConfig.WebSocketAuthenticationProvider;
import grp4.authentication.domain.service.AccountService;
import grp4.authentication.domain.service.IAccountService;
import grp4.authentication.util.Hasher;
import grp4.authentication.util.IHasher;
import grp4.authentication.util.JwtProvider;
import grp4.authentication.web.AccountController;
import grp4.common.config.Config;
import grp4.common.spi.IAccessManagerService;
import grp4.common.spi.IRouterService;
import grp4.common.spi.IWebSocketAuthenticationService;
import grp4.common.util.SPILocator;
import grp4.commonAuthentication.config.authConfig.Role;
import grp4.commonAuthentication.domain.repository.IAccountRepository;
import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.apibuilder.EndpointGroup;
import org.springframework.stereotype.Service;

import java.util.Set;

import static grp4.common.util.JavalinUtils.roles;
import static io.javalin.apibuilder.ApiBuilder.*;

@Service
public class AuthenticationProvider implements IRouterService, IAccessManagerService, IWebSocketAuthenticationService {
    private JwtProvider jwtProvider = new JwtProvider();
    private AuthConfig authConfig = new AuthConfig(jwtProvider);
    private IWebSocketAuthenticationService webSocketAuthenticationService = new WebSocketAuthenticationProvider(jwtProvider);
    private IHasher hasher = new Hasher();

    private AccountController accountController;

    public AuthenticationProvider() {
        IAccountRepository accountRepository = SPILocator.locateSpecific(IAccountRepository.class);
        IAccountService accountService = new AccountService(accountRepository, jwtProvider, hasher,
                Integer.parseInt(Config.getInstance().getProperty("auth.maxLoginAttempts")),
                Config.getInstance().getProperty("auth.allowedAdminIPs"));
        accountController = new AccountController(accountService);
    }

    @Override
    public EndpointGroup getRoutes() {
        return (() -> {
            path("/accounts", () -> {
                post(accountController::register, roles(Role.ANYONE));
                post("/login", accountController::login, roles(Role.ANYONE));

                get(accountController::getAllAccounts, roles(Role.ADMIN));
                post("/unlock/:account-id", accountController::unlock, roles(Role.ADMIN));
            });
        });
    }

    @Override
    public void configure(Handler handler, Context ctx, Set<io.javalin.security.Role> permittedRoles) throws Exception {
        authConfig.configure(handler, ctx, permittedRoles);
    }

    @Override
    public boolean doesUserHaveRole(Set<io.javalin.security.Role> permittedRoles, String authMsg) {
        return webSocketAuthenticationService.doesUserHaveRole(permittedRoles, authMsg);
    }

    @Override
    public String getUsername(String authMsg) {
        return webSocketAuthenticationService.getUsername(authMsg);
    }
}
