package com.recharge.phone.application.port.out;

import java.util.List;
import java.util.Optional;

import com.recharge.phone.domain.model.Phone;

public interface PhoneRepositoryPort {

    Phone save(Phone phone);

    List<Phone> findByUserId(String userId);

    Optional<Phone> findByIdAndUserId(String id, String userId);

    boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber);

    void deleteById(String id);

    void deleteAllByUserId(String userId);
}
