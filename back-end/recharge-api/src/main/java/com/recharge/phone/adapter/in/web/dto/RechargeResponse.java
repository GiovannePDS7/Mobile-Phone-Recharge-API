package com.recharge.phone.adapter.in.web.dto;

import java.time.LocalDateTime;

public record RechargeResponse(
    String id,
    String userId,
    String phoneNumber,
    double amount,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
