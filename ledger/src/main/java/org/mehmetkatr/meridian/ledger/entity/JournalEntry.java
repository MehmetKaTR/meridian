package org.mehmetkatr.meridian.ledger.entity;

import jakarta.persistence.*;
import lombok.*;
import org.mehmetkatr.meridian.common.base.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journal_entries")
@Getter
@NoArgsConstructor
public class JournalEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posting> postings = new ArrayList<>();

    public static JournalEntry create(String reference, String description, List<Posting> postings) {
        validateBalanced(postings);
        JournalEntry entry = new JournalEntry();
        entry.reference = reference;
        entry.description = description;
        for (Posting p : postings) {
            p.setJournalEntry(entry);
            entry.postings.add(p);
        }
        return entry;
    }

    private static void validateBalanced(List<Posting> postings) {
        if (postings == null || postings.size() < 2)
            throw new IllegalArgumentException("En az iki posting gerekir");
        BigDecimal debit = BigDecimal.ZERO, credit = BigDecimal.ZERO;
        for (Posting p : postings) {
            if (p.getDirection() == Direction.DEBIT) debit = debit.add(p.getAmount());
            else                                      credit = credit.add(p.getAmount());
        }
        if (debit.compareTo(credit) != 0)
            throw new IllegalArgumentException("Fis dengesiz: borc=" + debit + " alacak=" + credit);
    }
}
