package org.example.hw06_product_service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hw06_product_service.exception.ProductServiceException;
import org.example.hw06_product_service.mapper.PaymentMapper;
import org.example.payment_service.dao.model.generated.PaymentDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class PaymentService {

    private final RestClient restClient;
    private final PaymentMapper paymentMapper;

    public PaymentService(
            @Qualifier("productServiceRestClient") RestClient restClient,
            PaymentMapper paymentMapper
    ) {
        this.paymentMapper = paymentMapper;
        this.restClient = restClient;
    }

    public void putPayment(PaymentDto paymentDto) {
        Long productId = paymentDto.getProductId();
        log.info("Make payment for product with id %d".formatted(productId));
        restClient.put()
                .uri("/payment")
                .contentType(APPLICATION_JSON)
                .body(paymentMapper.paymentPaymentDtoToPaymentDto(paymentDto))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ProductServiceException(
                            "Failed to make payment. Product service error %d %s. Message: %s".formatted(
                                    response.getStatusCode().value(), response.getStatusText(), new String(response.getBody().readAllBytes())),
                            response.getStatusCode());
                })
                .toBodilessEntity();
        log.info("Successfully made payment for product with id %d".formatted(productId));
    }
}
