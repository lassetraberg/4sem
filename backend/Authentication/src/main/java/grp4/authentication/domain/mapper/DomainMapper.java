package grp4.authentication.domain.mapper;

import grp4.authentication.domain.dto.AccountDTO;
import grp4.commonAuthentication.domain.model.Account;

public class DomainMapper {
    public static Account toAccount(AccountDTO dto) {
        return new Account(
                null,
                dto.getUsername(),
                dto.getPassword(),
                null,
                null,
                0,
                null,
                dto.getRole()
        );
    }
}
