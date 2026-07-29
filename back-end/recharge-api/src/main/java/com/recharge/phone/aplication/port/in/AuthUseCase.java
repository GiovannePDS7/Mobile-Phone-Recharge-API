package com.recharge.phone.aplication.port.in;

import com.recharge.phone.domain.model.User;

public interface AuthUseCase {

    User register(String name, String email, String password);

    TokenResult login(String email, String password);

    TokenResult refreshToken(String refreshToken);
}
