package org.mehmetkatr.meridian.ledger.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.ledger.dto.request.CreateEntryRequest;
import org.mehmetkatr.meridian.ledger.dto.response.EntryResponse;
import org.mehmetkatr.meridian.ledger.entity.JournalEntry;
import org.mehmetkatr.meridian.ledger.entity.Posting;
import org.mehmetkatr.meridian.ledger.mapper.EntryMapper;
import org.mehmetkatr.meridian.ledger.repository.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final EntryMapper entryMapper;

    public EntryResponse createEntry(CreateEntryRequest request) {

        Optional<JournalEntry> existing = journalEntryRepository.findByReference(request.getReference());
        if (existing.isPresent()) {
            return entryMapper.toResponse(existing.get());
        }

        List<Posting> postings = request.getPostings().stream()
                .map(pr -> Posting.builder()
                        .ledgerAccountId(pr.getLedgerAccountId())
                        .direction(pr.getDirection())
                        .amount(pr.getAmount())
                        .build())
                .toList();

        JournalEntry entry = JournalEntry.create(
                request.getReference(),
                request.getDescription(),
                postings
        );

        JournalEntry saved = journalEntryRepository.save(entry);

        return entryMapper.toResponse(saved);
    }
}
