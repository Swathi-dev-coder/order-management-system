package com.oms.order_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	    @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", ex.getMessage());
	        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	    }

	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", ex.getMessage());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(Exception.class) // Catch-all
	    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
	    	ex.printStackTrace();
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Something went wrong");
	        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

