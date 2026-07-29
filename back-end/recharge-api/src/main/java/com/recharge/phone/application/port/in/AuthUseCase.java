package com.recharge.phone.application.port.in;

public interface AuthUseCase {

    TokenResult login(String email, String password);

    TokenResult refreshToken(String refreshToken);
}
