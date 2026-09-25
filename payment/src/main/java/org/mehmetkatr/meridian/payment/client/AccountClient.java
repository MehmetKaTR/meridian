package org.mehmetkatr.meridian.payment.client;

import org.mehmetkatr.meridian.payment.client.dto.AmountRequest;
import org.mehmetkatr.meridian.payment.client.dto.WalletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account")
public interface AccountClient {

    @PostMapping("/api/wallets/{walletId}/withdraw")
    WalletResponse withdraw(@PathVariable("walletId") Long walletId, @RequestBody AmountRequest request);

    @PostMapping("/api/wallets/{walletId}/deposit")
    WalletResponse deposit(@PathVariable("walletId") Long walletId, @RequestBody AmountRequest request);
}
