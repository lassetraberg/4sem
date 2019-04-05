package authentication.domain.mapper;

import authentication.domain.dto.AccountDTO;
import commonAuthentication.domain.model.Account;

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
