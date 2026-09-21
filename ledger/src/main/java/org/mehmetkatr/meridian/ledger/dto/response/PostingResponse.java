package org.mehmetkatr.meridian.ledger.dto.response;

import lombok.Data;
import org.mehmetkatr.meridian.ledger.entity.Direction;

import java.math.BigDecimal;

@Data
public class PostingResponse {
    private Long ledgerAccountId;
    private Direction direction;
    private BigDecimal amount;
}
