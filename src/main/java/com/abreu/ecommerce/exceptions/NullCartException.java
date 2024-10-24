package com.abreu.ecommerce.exceptions;

public class NullCartException extends RuntimeException {

    public NullCartException(String message) {
        super(message);
    }
}
