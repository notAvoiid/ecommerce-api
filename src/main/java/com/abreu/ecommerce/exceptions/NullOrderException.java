package com.abreu.ecommerce.exceptions;

public class NullOrderException extends RuntimeException {

    public NullOrderException(String message) {
        super(message);
    }
}
