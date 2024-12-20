package com.wanda.utils.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.wanda.utils.exception.CustomException;
import com.wanda.utils.response.ErrorResponse;

@RestControllerAdvice
public class GloabalExceptionHandler {

    // Handle specific exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleResourceNotFoundException(CustomException ex, WebRequest request) {
    	ErrorResponse errorDetails = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),	
            ex.getMessage(),
            request.getDescription(false)
        );
    
    	System.out.println("hadnled by global exception custom");
    	
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    	ErrorResponse errorDetails = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            request.getDescription(false)
        );
    	
    	System.out.println("hadnled by global exception");
    	ex.printStackTrace();
    	
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
