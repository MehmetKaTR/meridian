package org.mehmetkatr.meridian.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mehmetkatr.meridian.payment.dto.ExternalPaymentRequest;
import org.mehmetkatr.meridian.payment.dto.P2pTransferRequest;
import org.mehmetkatr.meridian.payment.dto.PaymentResponse;
import org.mehmetkatr.meridian.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/p2p")
    public ResponseEntity<PaymentResponse> p2p(@Valid @RequestBody P2pTransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.p2pTransfer(request));
    }

    @PostMapping("/external")
    public ResponseEntity<PaymentResponse> external(@Valid @RequestBody ExternalPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.externalTransfer(request));
    }
}
