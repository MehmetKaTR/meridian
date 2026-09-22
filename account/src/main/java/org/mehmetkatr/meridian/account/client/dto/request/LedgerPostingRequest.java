package org.mehmetkatr.meridian.account.client.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LedgerPostingRequest {

    private Long ledgerAccountId;

    private String direction;

    private BigDecimal amount;
}
