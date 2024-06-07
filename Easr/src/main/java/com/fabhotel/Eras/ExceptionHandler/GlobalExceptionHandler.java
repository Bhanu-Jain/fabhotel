package com.fabhotel.Eras.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
		
	

}
