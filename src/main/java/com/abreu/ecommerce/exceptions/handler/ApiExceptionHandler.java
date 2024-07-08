package com.abreu.ecommerce.exceptions.handler;

import com.abreu.ecommerce.exceptions.ErrorMessage;
import com.abreu.ecommerce.exceptions.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final String ERROR_PREFIX = "API Error - ";

    @ExceptionHandler({
            ProductNotFoundException.class,
    })
    public final ResponseEntity<ErrorMessage> handleEntityNotFoundException(
            RuntimeException ex, HttpServletRequest request
    ) {
        log.error(ERROR_PREFIX, ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }
}