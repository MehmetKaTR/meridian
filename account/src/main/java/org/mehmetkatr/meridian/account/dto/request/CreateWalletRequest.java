package org.mehmetkatr.meridian.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWalletRequest {

    @NotNull
    private Long accountId;

    @NotBlank
    private String currency;
}
