package com.fabhotel.Eras.ExceptionHandler;

public class RevieweeNotFoundException extends RuntimeException {
    public RevieweeNotFoundException(String message) {
        super(message);
    }
}
