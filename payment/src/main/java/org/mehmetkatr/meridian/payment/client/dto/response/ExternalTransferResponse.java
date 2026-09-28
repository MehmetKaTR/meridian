package org.mehmetkatr.meridian.payment.client.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExternalTransferResponse {
    private Long id;
    private String reference;
    private String status;
    private String toIban;
    private BigDecimal amount;
    private String currency;
}
