package com.fabhotel.Eras.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex,WebRequest request){
		Map<String,String>errorDetails = new HashMap<>();
		errorDetails.put("message", ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
		public ResponseEntity<?>handleValidationException(MethodArgumentNotValidException ex,WebRequest request){
	        Map<String, String> errorDetails = new HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errorDetails.put(error.getField(), error.getDefaultMessage())
        );
	        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
		}
		
		@ExceptionHandler(RevieweeNotFoundException.class)
	    public ResponseEntity<String> handleRevieweeNotFoundException(RevieweeNotFoundException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
		@ResponseStatus(HttpStatus.BAD_REQUEST)
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error -> 
	            errors.put(error.getField(), error.getDefaultMessage()));
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
		@ExceptionHandler({ReviewerNotFoundException.class, ReviewerNotFoundException.class})
	    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
		@ExceptionHandler({ValidationException.class, ValidationException.class})
	    public ResponseEntity<String> ValidationException(RuntimeException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
}
