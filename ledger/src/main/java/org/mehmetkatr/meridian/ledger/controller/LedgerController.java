package org.mehmetkatr.meridian.ledger.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.ledger.dto.request.CreateEntryRequest;
import org.mehmetkatr.meridian.ledger.dto.response.EntryResponse;
import org.mehmetkatr.meridian.ledger.service.LedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping("/entries")
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody CreateEntryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ledgerService.createEntry(request));
    }

}
