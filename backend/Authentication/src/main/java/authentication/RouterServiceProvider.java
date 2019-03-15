package authentication;

import authentication.domain.repository.AccountRepository;
import authentication.domain.service.AccountService;
import authentication.web.AccountController;
import common.spi.IRouterService;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RouterServiceProvider implements IRouterService {
    private AccountRepository accountRepository = new AccountRepository();
    private AccountService accountService = new AccountService(accountRepository);
    private AccountController accountController = new AccountController(accountService);
    public EndpointGroup getRoutes() {
        return (() -> {
            path("/accounts", () -> {
                post(accountController::register);
                post("/login", accountController::login);
            });
        });
    }
}
