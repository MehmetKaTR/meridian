package org.mehmetkatr.meridian.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "userId zorunlu")
    private Long userId;

    @NotBlank(message = "IBAN zorunlu")
    @Pattern(
            regexp = "^TR[0-9]{24}$",
            message = "Gecerli bir TR IBAN girin (TR + 24 rakam)"
    )
    private String iban;
}
