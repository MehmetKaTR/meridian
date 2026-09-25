package org.mehmetkatr.meridian.account.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.account.dto.request.AmountRequest;
import org.mehmetkatr.meridian.account.dto.request.CreateWalletRequest;
import org.mehmetkatr.meridian.account.dto.response.WalletResponse;
import org.mehmetkatr.meridian.account.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(request));
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<WalletResponse> withdraw(@PathVariable Long walletId,
                                                   @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(walletService.withdraw(walletId, request));
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<WalletResponse> deposit(@PathVariable Long walletId,
                                                  @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(walletService.deposit(walletId, request));
    }
}
