package org.mehmetkatr.meridian.ledger.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.mehmetkatr.meridian.ledger.dto.request.PostingRequest;

import java.util.List;

@Data
public class EntryResponse {

    @NotNull
    private Long id;

    @NotNull
    private String reference;

    private String description;

    private List<PostingResponse> postings;
}
