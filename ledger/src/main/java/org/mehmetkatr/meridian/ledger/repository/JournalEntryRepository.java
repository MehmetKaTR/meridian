package org.mehmetkatr.meridian.ledger.repository;

import org.mehmetkatr.meridian.ledger.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    Optional<JournalEntry> findByReference(String reference);
}
