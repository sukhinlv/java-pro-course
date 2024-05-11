package org.example.hw06_product_service.exception;

public class InsufficientFunds extends RuntimeException {

    public InsufficientFunds(String message) {
        super(message);
    }
}
