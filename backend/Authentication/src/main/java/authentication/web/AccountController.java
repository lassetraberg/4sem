package authentication.web;

import authentication.domain.Account;
import authentication.domain.dto.AccountDTO;
import authentication.domain.mapper.DomainMapper;
import authentication.domain.service.AccountService;
import io.javalin.Context;

public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void register(Context ctx) {
        AccountDTO user = ctx.validatedBodyAsClass(AccountDTO.class)
                .check(u -> !u.getUsername().isEmpty())
                .check(u -> !u.getPassword().isEmpty())
                .getOrThrow();
        Account createdAccount = accountService.create(DomainMapper.toAccount(user));
        ctx.json(createdAccount);
    }

    public void login(Context ctx) {
        AccountDTO user = ctx.validatedBodyAsClass(AccountDTO.class)
                .check(u -> !u.getUsername().isEmpty())
                .check(u -> !u.getPassword().isEmpty())
                .getOrThrow();
        Account authenticatedAccount = accountService.authenticate(DomainMapper.toAccount(user));
        ctx.json(authenticatedAccount);
    }
}
