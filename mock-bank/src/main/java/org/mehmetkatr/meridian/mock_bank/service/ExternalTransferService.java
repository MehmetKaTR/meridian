package org.mehmetkatr.meridian.mock_bank.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.mock_bank.dto.request.ExternalTransferRequest;
import org.mehmetkatr.meridian.mock_bank.dto.response.ExternalTransferResponse;
import org.mehmetkatr.meridian.mock_bank.entity.ExternalTransfer;
import org.mehmetkatr.meridian.mock_bank.entity.ExternalTransferStatus;
import org.mehmetkatr.meridian.mock_bank.repository.ExternalTransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalTransferService {

    private static final BigDecimal LIMIT = new BigDecimal("100000");

    private final ExternalTransferRepository externalTransferRepository;

    public ExternalTransferResponse process(ExternalTransferRequest req) {

        Optional<ExternalTransfer> existing = externalTransferRepository.findByReference(req.getReference());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        ExternalTransferStatus status;
        if (req.getToIban().contains("FAIL")) {
            status = ExternalTransferStatus.REJECTED;
        } else if (req.getAmount().compareTo(LIMIT) > 0) {
            status = ExternalTransferStatus.REJECTED;
        } else {
            status = ExternalTransferStatus.APPROVED;
        }

        ExternalTransfer transfer = ExternalTransfer.builder()
                .reference(req.getReference())
                .toIban(req.getToIban())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .status(status)
                .build();
        ExternalTransfer saved = externalTransferRepository.save(transfer);

        return toResponse(saved);
    }

    private ExternalTransferResponse toResponse(ExternalTransfer t) {
        ExternalTransferResponse r = new ExternalTransferResponse();
        r.setId(t.getId());
        r.setReference(t.getReference());
        r.setStatus(t.getStatus().name());
        r.setToIban(t.getToIban());
        r.setAmount(t.getAmount());
        r.setCurrency(t.getCurrency());
        return r;
    }
}