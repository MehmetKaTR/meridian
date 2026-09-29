package org.mehmetkatr.meridian.payment.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.common.money.Money;
import org.mehmetkatr.meridian.payment.client.AccountClient;
import org.mehmetkatr.meridian.payment.client.LedgerClient;
import org.mehmetkatr.meridian.payment.client.dto.request.AmountRequest;
import org.mehmetkatr.meridian.payment.client.dto.request.ExternalTransferRequest;
import org.mehmetkatr.meridian.payment.client.dto.request.LedgerEntryRequest;
import org.mehmetkatr.meridian.payment.client.dto.request.LedgerPostingRequest;
import org.mehmetkatr.meridian.payment.client.dto.response.ExternalTransferResponse;
import org.mehmetkatr.meridian.payment.dto.ExternalPaymentRequest;
import org.mehmetkatr.meridian.payment.dto.P2pTransferRequest;
import org.mehmetkatr.meridian.payment.dto.PaymentResponse;
import org.mehmetkatr.meridian.payment.entity.*;
import org.mehmetkatr.meridian.payment.gateway.MockBankGateway;
import org.mehmetkatr.meridian.payment.repository.OutboxRepository;
import org.mehmetkatr.meridian.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final AccountClient accountClient;
    private final LedgerClient ledgerClient;
    private final MockBankGateway mockBankGateway;

    private final ObjectMapper objectMapper;

    @Transactional
    public PaymentResponse p2pTransfer(P2pTransferRequest request) {
        Optional<Payment> existing = paymentRepository.findByReference(request.getReference());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        Payment payment = Payment.builder()
                .reference(request.getReference())
                .fromWalletId(request.getFromWalletId())
                .toWalletId(request.getToWalletId())
                .type(PaymentType.P2P)
                .status(PaymentStatus.PENDING)
                .amount(new Money(request.getAmount(), request.getCurrency()))
                .build();
        paymentRepository.save(payment);

        AmountRequest amountReq = amountRequest(request.getAmount(), request.getCurrency());

        boolean withdrawn = false;
        boolean deposited = false;
        try {
            accountClient.withdraw(request.getFromWalletId(), amountReq);   withdrawn = true;
            accountClient.deposit(request.getToWalletId(), amountReq);      deposited = true;
            ledgerClient.createEntry(buildLedgerEntry(
                    request.getReference(), request.getFromLedgerAccountId(), request.getToLedgerAccountId(),
                    request.getAmount(), "P2P " + request.getFromWalletId() + " -> " + request.getToWalletId()));
            payment.setStatus(PaymentStatus.COMPLETED);
            saveOutboxEvent(payment);
        } catch (Exception e) {
            if (deposited) accountClient.withdraw(request.getToWalletId(), amountReq);
            if (withdrawn) accountClient.deposit(request.getFromWalletId(), amountReq);
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse externalTransfer(ExternalPaymentRequest request) {
        Optional<Payment> existing = paymentRepository.findByReference(request.getReference());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        Payment payment = Payment.builder()
                .reference(request.getReference())
                .fromWalletId(request.getFromWalletId())
                .toIban(request.getToIban())
                .type(PaymentType.EXTERNAL)
                .status(PaymentStatus.PENDING)
                .amount(new Money(request.getAmount(), request.getCurrency()))
                .build();
        paymentRepository.save(payment);

        AmountRequest amountReq = amountRequest(request.getAmount(), request.getCurrency());

        boolean withdrawn = false;
        try {
            accountClient.withdraw(request.getFromWalletId(), amountReq);
            withdrawn = true;

            ExternalTransferRequest bankReq = new ExternalTransferRequest();
            bankReq.setReference(request.getReference());
            bankReq.setToIban(request.getToIban());
            bankReq.setAmount(request.getAmount());
            bankReq.setCurrency(request.getCurrency());
            ExternalTransferResponse bankResp = mockBankGateway.sendTransfer(bankReq);

            if ("APPROVED".equals(bankResp.getStatus())) {
                ledgerClient.createEntry(buildLedgerEntry(
                        request.getReference(), request.getFromLedgerAccountId(), request.getToLedgerAccountId(),
                        request.getAmount(), "EXTERNAL " + request.getFromWalletId() + " -> " + request.getToIban()));
                payment.setStatus(PaymentStatus.COMPLETED);
                saveOutboxEvent(payment);
            } else {
                if (withdrawn) accountClient.deposit(request.getFromWalletId(), amountReq);
                payment.setStatus(PaymentStatus.FAILED);
            }
        } catch (Exception e) {
            if (withdrawn) accountClient.deposit(request.getFromWalletId(), amountReq);
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
        return toResponse(payment);
    }

    private AmountRequest amountRequest(BigDecimal amount, String currency) {
        AmountRequest req = new AmountRequest();
        req.setAmount(amount);
        req.setCurrency(currency);
        return req;
    }

    private LedgerEntryRequest buildLedgerEntry(String reference, Long fromLedgerAccountId,
                                               Long toLedgerAccountId, BigDecimal amount, String description) {
        LedgerPostingRequest debit = new LedgerPostingRequest();
        debit.setLedgerAccountId(fromLedgerAccountId);
        debit.setDirection("DEBIT");
        debit.setAmount(amount);

        LedgerPostingRequest credit = new LedgerPostingRequest();
        credit.setLedgerAccountId(toLedgerAccountId);
        credit.setDirection("CREDIT");
        credit.setAmount(amount);

        LedgerEntryRequest entry = new LedgerEntryRequest();
        entry.setReference(reference);
        entry.setDescription(description);
        entry.setPostings(List.of(debit, credit));
        return entry;
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse r = new PaymentResponse();
        r.setId(payment.getId());
        r.setReference(payment.getReference());
        r.setType(payment.getType().name());
        r.setFromWalletId(payment.getFromWalletId());
        r.setToWalletId(payment.getToWalletId());
        r.setToIban(payment.getToIban());
        r.setAmount(payment.getAmount().getAmount());
        r.setCurrency(payment.getAmount().getCurrency());
        r.setStatus(payment.getStatus().name());
        return r;
    }

    private void saveOutboxEvent(Payment payment) {
        var eventData = java.util.Map.of(
                "paymentId", payment.getId(),
                "reference", payment.getReference(),
                "amount", payment.getAmount().getAmount(),
                "currency", payment.getAmount().getCurrency(),
                "status", payment.getStatus().name(),
                "type", payment.getType().name()
        );
        OutboxEvent event = OutboxEvent.builder()
                .aggregateId(payment.getId())
                .eventType("PaymentCompleted")
                .payload(objectMapper.writeValueAsString(eventData))
                .status(OutboxStatus.PENDING)
                .build();
        outboxRepository.save(event);
    }
}
