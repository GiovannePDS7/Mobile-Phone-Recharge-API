package com.recharge.phone.aplication.port.in;

public record TokenResult(String accessToken, String refreshToken, long expiresIn) {
}
