package org.example.hw07_product_service.exception;

public class InsufficientFunds extends RuntimeException {

    public InsufficientFunds(String message) {
        super(message);
    }
}
