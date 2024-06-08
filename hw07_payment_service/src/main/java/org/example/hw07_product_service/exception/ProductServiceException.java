package org.example.hw07_product_service.exception;

import org.springframework.http.HttpStatusCode;

public class ProductServiceException extends BaseException {

    public ProductServiceException(String message, HttpStatusCode statusCode) {
        super(message, statusCode);
    }
}
