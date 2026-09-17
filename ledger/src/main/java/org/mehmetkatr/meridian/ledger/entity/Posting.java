package org.mehmetkatr.meridian.ledger.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mehmetkatr.meridian.common.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "postings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @Column(name = "ledger_account_id", nullable = false)
    private Long ledgerAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private BigDecimal amount;
}
