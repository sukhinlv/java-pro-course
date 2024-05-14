package org.example.hw06_product_service.exception;

import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class BaseException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatusCode statusCode;

    private final String message;

    public BaseException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
