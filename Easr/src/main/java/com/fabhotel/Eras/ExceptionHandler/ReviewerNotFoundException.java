package com.fabhotel.Eras.ExceptionHandler;

public class ReviewerNotFoundException extends RuntimeException{
	public ReviewerNotFoundException(String message) {
        super(message);
    }
}
