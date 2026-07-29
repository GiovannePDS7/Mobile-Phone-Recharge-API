package com.recharge.phone.application.port.in;

public record TokenResult(String accessToken, String refreshToken, long expiresIn) {
}
