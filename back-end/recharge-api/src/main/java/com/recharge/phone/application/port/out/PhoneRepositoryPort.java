package com.recharge.phone.application.port.out;

import java.util.List;
import java.util.Optional;

import com.recharge.phone.domain.model.Phone;

public interface PhoneRepositoryPort {

    Phone save(Phone phone);

    List<Phone> findByUserId(String userId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber);

    void deleteById(String id);

    Optional<Phone> findByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String userId);

    Optional<Phone> findByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
