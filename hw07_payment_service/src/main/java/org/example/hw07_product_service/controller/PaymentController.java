package org.example.hw07_product_service.controller;

import lombok.RequiredArgsConstructor;
import org.example.hw07_product_service.service.PaymentService;
import org.example.payment_service.api.PaymentControllerApi;
import org.example.payment_service.dao.model.generated.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<Void> putPayment(PaymentDto paymentDto) {
        paymentService.putPayment(paymentDto);
        return ResponseEntity.ok().build();
    }
}
