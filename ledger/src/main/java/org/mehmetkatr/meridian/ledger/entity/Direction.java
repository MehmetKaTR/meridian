package org.mehmetkatr.meridian.ledger.entity;

import java.math.BigDecimal;

public enum Direction {
    DEBIT, CREDIT;

    public BigDecimal signed(BigDecimal amount) {
        return this == Direction.CREDIT ? amount : amount.negate();
    }
}
