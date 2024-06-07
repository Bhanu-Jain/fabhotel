package com.fabhotel.Eras.ExceptionHandler;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

