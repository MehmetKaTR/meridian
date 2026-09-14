package org.mehmetkatr.meridian.account.mapper;

import org.mehmetkatr.meridian.account.dto.response.AccountResponse;
import org.mehmetkatr.meridian.account.entity.Account;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account user){
        AccountResponse response = new AccountResponse();
        response.setId(user.getId());
        response.setUserId(user.getUserId());
        response.setIban(user.getIban());
        response.setStatus(user.getStatus().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    public List<AccountResponse> toResponse(List<Account> accounts){
        return accounts.stream().map(this::toResponse).toList();
    }

}
