package org.example.hw07_product_service.exception;

import org.springframework.http.HttpStatusCode;

public class PaymentServiceException extends BaseException {

    public PaymentServiceException(String message, HttpStatusCode statusCode) {
        super(message, statusCode);
    }
}
