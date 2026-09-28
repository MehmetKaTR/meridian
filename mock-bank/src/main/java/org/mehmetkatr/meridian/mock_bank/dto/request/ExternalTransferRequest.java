package org.mehmetkatr.meridian.mock_bank.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExternalTransferRequest {

    @NotBlank
    private String reference;

    @NotBlank
    private String toIban;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;
}
