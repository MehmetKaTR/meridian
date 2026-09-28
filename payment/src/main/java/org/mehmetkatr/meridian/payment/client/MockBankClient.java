package org.mehmetkatr.meridian.payment.client;

import org.mehmetkatr.meridian.payment.client.dto.request.ExternalTransferRequest;
import org.mehmetkatr.meridian.payment.client.dto.response.ExternalTransferResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="mock-bank")
public interface MockBankClient {
    @PostMapping("/api/mock-bank/transfers")
    ExternalTransferResponse transfer(@RequestBody ExternalTransferRequest request);
}
