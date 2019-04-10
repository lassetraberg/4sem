package authentication.web;

import authentication.domain.dto.AccountDTO;
import authentication.domain.mapper.DomainMapper;
import authentication.domain.service.IAccountService;
import common.domain.model.Response;
import commonAuthentication.domain.model.Account;
import io.javalin.Context;
import org.eclipse.jetty.http.HttpStatus;

public class AccountController {
    private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    public void register(Context ctx) {
        AccountDTO user = getOrThrow(ctx);
        Account createdAccount = accountService.create(DomainMapper.toAccount(user), ctx.ip());
        ctx.json(createdAccount);
    }

    public void login(Context ctx) {
        AccountDTO user = getOrThrow(ctx);
        Account authenticatedAccount = accountService.authenticate(DomainMapper.toAccount(user));
        ctx.json(authenticatedAccount);
    }

    public void unlock(Context ctx) {
        Long accountId = Long.valueOf(ctx.pathParam("account-id"));
        boolean successfullyUnlocked = accountService.unlockAccount(accountId);

        Response res;
        if (successfullyUnlocked) {
            res = new Response(HttpStatus.OK_200, String.format("Successfully unlocked account with id %d", accountId));
        } else {
            res = new Response(HttpStatus.BAD_REQUEST_400, String.format("Failed to unlocked account with id %d", accountId));
        }

        ctx.json(res);
    }

    public void getAllAccounts(Context ctx) {
        ctx.json(accountService.getAllAccounts());
    }

    private AccountDTO getOrThrow(Context ctx) {
        return ctx.validatedBodyAsClass(AccountDTO.class)
                .check(u -> u.getUsername() != null && !u.getUsername().isEmpty())
                .check(u -> u.getPassword() != null && !u.getPassword().isEmpty())
                .getOrThrow();
    }
}
