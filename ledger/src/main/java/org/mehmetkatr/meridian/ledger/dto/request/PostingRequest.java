package org.mehmetkatr.meridian.ledger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.mehmetkatr.meridian.ledger.entity.Direction;

import java.math.BigDecimal;

@Data
public class PostingRequest {

    @NotNull
    private Long ledgerAccountId;

    @NotNull
    private Direction direction;

    @NotNull
    @Positive
    private BigDecimal amount;
}
