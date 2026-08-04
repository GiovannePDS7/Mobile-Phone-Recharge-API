package com.recharge.phone.domain.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Invalid token");
    }
}