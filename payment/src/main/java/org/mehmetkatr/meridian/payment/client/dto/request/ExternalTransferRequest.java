package org.mehmetkatr.meridian.payment.client.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExternalTransferRequest {
    private String reference;
    private String toIban;
    private BigDecimal amount;
    private String currency;
}
