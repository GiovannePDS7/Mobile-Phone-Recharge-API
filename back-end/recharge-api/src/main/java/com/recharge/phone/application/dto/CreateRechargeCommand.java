package com.recharge.phone.application.dto;

public record CreateRechargeCommand(String userId, String phoneNumber, double amount) {}
