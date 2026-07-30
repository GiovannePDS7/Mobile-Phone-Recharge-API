package com.recharge.phone.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataUserRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
