package org.mehmetkatr.meridian.payment.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletResponse {
    private Long id;
    private Long accountId;
    private String currency;
    private BigDecimal balance;
}
