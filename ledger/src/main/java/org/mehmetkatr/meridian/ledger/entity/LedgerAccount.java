package org.mehmetkatr.meridian.ledger.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ledger_accounts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerAccount extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "account_ref")
    private Long accountRef;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerAccountType type;

    public enum LedgerAccountType{
        CUSTOMER,EXTERNAL
    }
}