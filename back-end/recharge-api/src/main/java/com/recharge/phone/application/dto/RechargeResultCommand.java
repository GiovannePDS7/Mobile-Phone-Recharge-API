package com.recharge.phone.application.dto;

import java.time.LocalDateTime;

public record RechargeResultCommand(
    String id,
    String userId,
    String phoneNumber,
    double amount,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
