package com.recharge.phone.domain.exception;

public class PhoneNotFoundException extends RuntimeException {

    public PhoneNotFoundException() {
        super("Phone not found");
    }
}