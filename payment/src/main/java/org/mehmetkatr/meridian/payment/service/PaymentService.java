package org.mehmetkatr.meridian.payment.service;

import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.common.money.Money;
import org.mehmetkatr.meridian.payment.client.AccountClient;
import org.mehmetkatr.meridian.payment.client.LedgerClient;
import org.mehmetkatr.meridian.payment.client.dto.AmountRequest;
import org.mehmetkatr.meridian.payment.client.dto.LedgerEntryRequest;
import org.mehmetkatr.meridian.payment.client.dto.LedgerPostingRequest;
import org.mehmetkatr.meridian.payment.dto.PaymentResponse;
import org.mehmetkatr.meridian.payment.dto.TransferRequest;
import org.mehmetkatr.meridian.payment.entity.Payment;
import org.mehmetkatr.meridian.payment.entity.PaymentStatus;
import org.mehmetkatr.meridian.payment.entity.PaymentType;
import org.mehmetkatr.meridian.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountClient accountClient;
    private final LedgerClient ledgerClient;

    @Transactional
    public PaymentResponse transfer(TransferRequest request) {

        boolean withdrawn = false;
        boolean deposited = false;

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

        AmountRequest amountReq = new AmountRequest();
        amountReq.setAmount(request.getAmount());
        amountReq.setCurrency(request.getCurrency());

        try {
            accountClient.withdraw(request.getFromWalletId(), amountReq);   withdrawn = true;
            accountClient.deposit(request.getToWalletId(), amountReq);      deposited = true;
            ledgerClient.createEntry(buildLedgerEntry(request));
            payment.setStatus(PaymentStatus.COMPLETED);
        } catch (Exception e) {
            if (deposited) {
                accountClient.withdraw(request.getToWalletId(), amountReq);
            }
            if (withdrawn) {
                accountClient.deposit(request.getFromWalletId(), amountReq);
            }
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
        return toResponse(payment);
    }

    private LedgerEntryRequest buildLedgerEntry(TransferRequest request) {
        LedgerPostingRequest debit = new LedgerPostingRequest();
        debit.setLedgerAccountId(request.getFromLedgerAccountId());
        debit.setDirection("DEBIT");
        debit.setAmount(request.getAmount());

        LedgerPostingRequest credit = new LedgerPostingRequest();
        credit.setLedgerAccountId(request.getToLedgerAccountId());
        credit.setDirection("CREDIT");
        credit.setAmount(request.getAmount());

        LedgerEntryRequest entry = new LedgerEntryRequest();
        entry.setReference(request.getReference());
        entry.setDescription("Payment " + request.getFromWalletId() + " -> " + request.getToWalletId());
        entry.setPostings(List.of(debit, credit));
        return entry;
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse r = new PaymentResponse();
        r.setId(payment.getId());
        r.setReference(payment.getReference());
        r.setFromWalletId(payment.getFromWalletId());
        r.setToWalletId(payment.getToWalletId());
        r.setAmount(payment.getAmount().getAmount());
        r.setCurrency(payment.getAmount().getCurrency());
        r.setStatus(payment.getStatus().name());
        return r;
    }
}
