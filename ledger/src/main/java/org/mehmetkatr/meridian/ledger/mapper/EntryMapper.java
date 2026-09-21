package org.mehmetkatr.meridian.ledger.mapper;

import org.mehmetkatr.meridian.ledger.dto.response.EntryResponse;
import org.mehmetkatr.meridian.ledger.dto.response.PostingResponse;
import org.mehmetkatr.meridian.ledger.entity.JournalEntry;
import org.mehmetkatr.meridian.ledger.entity.Posting;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntryMapper {

    public EntryResponse toResponse(JournalEntry entry) {
        EntryResponse response = new EntryResponse();
        response.setId(entry.getId());
        response.setReference(entry.getReference());
        response.setDescription(entry.getDescription());
        response.setPostings(toPostingResponses(entry.getPostings()));
        return response;
    }

    private PostingResponse toPostingResponse(Posting posting) {
        PostingResponse pr = new PostingResponse();
        pr.setLedgerAccountId(posting.getLedgerAccountId());
        pr.setDirection(posting.getDirection());
        pr.setAmount(posting.getAmount());
        return pr;
    }

    private List<PostingResponse> toPostingResponses(List<Posting> postings) {
        return postings.stream()
                .map(this::toPostingResponse)
                .toList();
    }
}
