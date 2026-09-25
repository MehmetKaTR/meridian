package org.mehmetkatr.meridian.payment.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private Long id;
    private String reference;
    private Long fromWalletId;
    private Long toWalletId;
    private BigDecimal amount;
    private String currency;
    private String status;
}
