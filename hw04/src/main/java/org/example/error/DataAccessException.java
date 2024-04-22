package org.example.error;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
