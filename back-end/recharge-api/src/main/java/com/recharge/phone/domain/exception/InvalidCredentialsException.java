package com.recharge.phone.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}