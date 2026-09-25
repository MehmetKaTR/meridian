package org.mehmetkatr.meridian.payment.client;

import org.mehmetkatr.meridian.payment.client.dto.LedgerEntryRequest;
import org.mehmetkatr.meridian.payment.client.dto.LedgerEntryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ledger")
public interface LedgerClient {
    @PostMapping("/api/ledger/entries")
    LedgerEntryResponse createEntry(@RequestBody LedgerEntryRequest request);
}
