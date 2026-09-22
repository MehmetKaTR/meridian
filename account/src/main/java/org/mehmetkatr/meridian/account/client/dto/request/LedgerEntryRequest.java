package org.mehmetkatr.meridian.account.client.request;

import lombok.Data;

import java.util.List;

@Data
public class LedgerEntryRequest {

    private String reference;

    private String description;

    private List<LedgerPostingRequest> postings;
}
