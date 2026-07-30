package com.recharge.phone.application.port.in;

import com.recharge.phone.domain.model.User;

public interface UserUseCase {

    User register(String name, String email, String password);

    User getProfile(String userId);

    User update(String userId, String name, String email, String password);

    void delete(String userId);
}
