package com.recharge.phone.domain.exception;

public class RechargeNotFoundException extends RuntimeException {

    public RechargeNotFoundException(String id) {
        super("Recarga não encontrada: " + id);
    }
}
