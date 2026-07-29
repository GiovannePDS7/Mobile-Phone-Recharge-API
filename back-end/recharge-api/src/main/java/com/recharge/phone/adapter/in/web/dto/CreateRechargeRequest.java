package com.recharge.phone.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateRechargeRequest(
    @NotBlank
    String userId,
    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{10,14}$", message = "Invalid phone number format")
    String phoneNumber,
    @DecimalMin(value = "10.0", message = "Minimum recharge amount is R$ 10.00")
    @DecimalMax(value = "200.0", message = "Maximum recharge amount is R$ 200.00")
    double amount
) {}
