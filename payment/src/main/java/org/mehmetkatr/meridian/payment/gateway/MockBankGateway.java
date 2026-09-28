package org.mehmetkatr.meridian.payment.gateway;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.payment.client.MockBankClient;
import org.mehmetkatr.meridian.payment.client.dto.request.ExternalTransferRequest;
import org.mehmetkatr.meridian.payment.client.dto.response.ExternalTransferResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockBankGateway {

    private final MockBankClient mockBankClient;

    @CircuitBreaker(name="mockBank", fallbackMethod="fallback")
    public ExternalTransferResponse sendTransfer(ExternalTransferRequest req) {   // Throwable YOK
        return mockBankClient.transfer(req);
    }

    public ExternalTransferResponse fallback(ExternalTransferRequest req, Throwable t) {
        ExternalTransferResponse r = new ExternalTransferResponse();
        r.setReference(req.getReference());
        r.setStatus("REJECTED");
        r.setToIban(req.getToIban());
        r.setAmount(req.getAmount());
        r.setCurrency(req.getCurrency());

        return r;
    }
}
