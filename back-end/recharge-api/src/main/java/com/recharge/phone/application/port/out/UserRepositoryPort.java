package com.recharge.phone.application.port.out;

import com.recharge.phone.domain.model.User;
import java.util.Optional;


public interface UserRepositoryPort {

    User createUser(User user);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}