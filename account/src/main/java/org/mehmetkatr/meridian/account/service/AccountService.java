package org.mehmetkatr.meridian.account.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.account.dto.request.CreateAccountRequest;
import org.mehmetkatr.meridian.account.dto.response.AccountResponse;
import org.mehmetkatr.meridian.account.entity.Account;
import org.mehmetkatr.meridian.account.mapper.AccountMapper;
import org.mehmetkatr.meridian.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountResponse createAccount(CreateAccountRequest request){

        Account newUser = Account.builder()
                .userId(request.getUserId())
                .iban(request.getIban())
                .status(Account.AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(newUser);
        return accountMapper.toResponse(saved);
    }

    public List<AccountResponse> getAllAccounts(){
        return accountMapper.toResponse(accountRepository.findAll());
    }

}
