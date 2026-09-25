package org.mehmetkatr.meridian.payment.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AmountRequest {
    private BigDecimal amount;
    private String currency;
}
