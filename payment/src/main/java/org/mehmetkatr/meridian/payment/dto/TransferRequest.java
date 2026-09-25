package org.mehmetkatr.meridian.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull
    private Long fromWalletId;

    @NotNull
    private Long toWalletId;

    @NotNull
    private Long fromLedgerAccountId;

    @NotNull
    private Long toLedgerAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String reference;
}