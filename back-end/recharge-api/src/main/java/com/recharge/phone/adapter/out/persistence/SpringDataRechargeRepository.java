package com.recharge.phone.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

interface SpringDataRechargeRepository extends MongoRepository<RechargeDocument, String> {

    Page<RechargeDocument> findByUserId(String userId, Pageable pageable);

    long countByUserId(String userId);
}
