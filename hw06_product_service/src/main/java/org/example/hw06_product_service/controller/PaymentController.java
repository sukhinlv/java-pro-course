package org.example.hw06_product_service.controller;

import lombok.RequiredArgsConstructor;
import org.example.hw06_product_service.service.PaymentService;
import org.example.product_service.api.PaymentControllerApi;
import org.example.product_service.dao.model.generated.PaymentDto;
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
