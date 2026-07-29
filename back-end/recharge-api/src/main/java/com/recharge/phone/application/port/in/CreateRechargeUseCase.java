package com.recharge.phone.application.port.in;

public interface CreateRechargeUseCase {
    void createRecharge(String phoneNumber, double amount);
}