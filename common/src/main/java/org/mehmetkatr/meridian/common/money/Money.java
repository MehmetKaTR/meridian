package org.mehmetkatr.meridian.common.money;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Getter
public class Money {

    private BigDecimal amount;
    private String currency;

    protected Money(){}

    public Money(BigDecimal amount, String currency){

        if (amount == null) throw new IllegalArgumentException("Tutar bos olamaz");
        if (amount.signum() < 0) throw new IllegalArgumentException("Tutar negatif olamaz");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Para birimi zorunlu");

        this.amount = amount;
        this.currency = currency;
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException("Farkli para birimleri islenemez: " + this.currency + " vs " + other.currency);
    }

    public boolean isGreaterThanOrEqualTo(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money m)) return false;
        return amount.compareTo(m.amount) == 0 && currency.equals(m.currency);
    }
    @Override public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }
}
