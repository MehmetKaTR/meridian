package org.mehmetkatr.meridian.ledger.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.mehmetkatr.meridian.ledger.entity.Posting;

import java.util.List;

@Data
public class CreateEntryRequest {

    @NotBlank
    private String reference;

    private String description;

    @NotEmpty
    @Valid
    private List<PostingRequest> postings;
}
