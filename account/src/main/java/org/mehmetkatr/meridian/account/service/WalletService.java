package org.mehmetkatr.meridian.account.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.account.dto.request.AmountRequest;
import org.mehmetkatr.meridian.account.dto.request.CreateWalletRequest;
import org.mehmetkatr.meridian.account.dto.response.WalletResponse;
import org.mehmetkatr.meridian.account.entity.Wallet;
import org.mehmetkatr.meridian.account.repository.WalletRepository;
import org.mehmetkatr.meridian.common.money.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletResponse createWallet(CreateWalletRequest request) {
        Money initialBalance = new Money(BigDecimal.ZERO, request.getCurrency());

        Wallet wallet = Wallet.builder()
                .accountId(request.getAccountId())
                .balance(initialBalance)
                .build();

        return toResponse(walletRepository.save(wallet));
    }

    public WalletResponse withdraw(Long walletId, AmountRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Cuzdan bulunamadi: " + walletId));

        wallet.withdraw(new Money(request.getAmount(), request.getCurrency()));

        return toResponse(walletRepository.save(wallet));
    }


    public WalletResponse deposit(Long walletId, AmountRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Cuzdan bulunamadi: " + walletId));

        wallet.deposit(new Money(request.getAmount(), request.getCurrency()));

        return toResponse(walletRepository.save(wallet));
    }

    private WalletResponse toResponse(Wallet wallet) {
        WalletResponse response = new WalletResponse();
        response.setId(wallet.getId());
        response.setAccountId(wallet.getAccountId());
        response.setCurrency(wallet.getBalance().getCurrency());
        response.setBalance(wallet.getBalance().getAmount());
        response.setCreatedAt(wallet.getCreatedAt());
        return response;
    }
}
