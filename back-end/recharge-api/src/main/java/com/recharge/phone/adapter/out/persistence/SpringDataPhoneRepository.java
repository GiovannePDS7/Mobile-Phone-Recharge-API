package com.recharge.phone.adapter.out.persistence;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataPhoneRepository extends MongoRepository<PhoneDocument, String> {

    List<PhoneDocument> findByUserId(String userId);

    boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber);

    void deleteAllByUserId(String userId);

    boolean existsByPhoneNumber(String phoneNumber);
}
