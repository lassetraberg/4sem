package authentication.web;

import authentication.domain.dto.AccountDTO;
import authentication.domain.mapper.DomainMapper;
import authentication.domain.service.IAccountService;
import commonAuthentication.domain.model.Account;
import io.javalin.Context;

public class AccountController {
    private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    public void register(Context ctx) {
        AccountDTO user = getOrThrow(ctx);
        Account createdAccount = accountService.create(DomainMapper.toAccount(user));
        ctx.json(createdAccount);
    }

    public void login(Context ctx) {
        AccountDTO user = getOrThrow(ctx);
        Account authenticatedAccount = accountService.authenticate(DomainMapper.toAccount(user));
        ctx.json(authenticatedAccount);
    }

    private AccountDTO getOrThrow(Context ctx) {
        return ctx.validatedBodyAsClass(AccountDTO.class)
                .check(u -> u.getUsername() != null && !u.getUsername().isEmpty())
                .check(u -> u.getPassword() != null && !u.getPassword().isEmpty())
                .getOrThrow();
    }
}
