package org.mehmetkatr.meridian.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mehmetkatr.meridian.common.base.BaseEntity;
import org.mehmetkatr.meridian.common.money.Money;


@Entity
@Table(name="payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(name = "from_wallet_id", nullable = false)
    private Long fromWalletId;

    @Column(name = "to_wallet_id")
    private Long toWalletId;

    @Column(name = "to_iban")
    private String toIban;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Embedded
    private Money amount;
}
