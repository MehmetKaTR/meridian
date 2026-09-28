package org.mehmetkatr.meridian.mock_bank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.mock_bank.dto.request.ExternalTransferRequest;
import org.mehmetkatr.meridian.mock_bank.dto.response.ExternalTransferResponse;
import org.mehmetkatr.meridian.mock_bank.service.ExternalTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mock-bank")
@RequiredArgsConstructor
public class ExternalTransferController {

    private final ExternalTransferService externalTransferService;

    @PostMapping("/transfers")
    public ResponseEntity<ExternalTransferResponse> transfer(
            @Valid @RequestBody ExternalTransferRequest request) {
        return ResponseEntity.ok(externalTransferService.process(request));
    }
}
