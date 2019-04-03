package authentication.domain.mapper;

import commonAuthentication.domain.model.Account;
import authentication.domain.dto.AccountDTO;

public class DomainMapper {
    public static Account toAccount(AccountDTO dto) {
        return new Account(
                null,
                dto.getUsername(),
                dto.getPassword(),
                null,
                null,
                -1,
                null
        );
    }
}
