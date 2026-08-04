package com.recharge.phone.domain.exception;

public class PhoneAlreadyRegisteredException extends RuntimeException {

    public PhoneAlreadyRegisteredException() {
        super("Phone number already registered");
    }
}