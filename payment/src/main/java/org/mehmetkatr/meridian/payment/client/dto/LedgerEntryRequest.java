package org.mehmetkatr.meridian.payment.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class LedgerEntryRequest {
    private String reference;
    private String description;
    private List<LedgerPostingRequest> postings;
}
