package org.mehmetkatr.meridian.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mehmetkatr.meridian.common.base.BaseEntity;
import org.mehmetkatr.meridian.common.money.Money;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount",   column = @Column(name = "balance",  nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money balance;

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (!this.balance.isGreaterThanOrEqualTo(amount)) {
            throw new IllegalArgumentException("Yetersiz bakiye");
        }
        this.balance = this.balance.subtract(amount);
    }
}
