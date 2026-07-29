package com.recharge.phone.application.port.out;

import java.util.List;
import java.util.Optional;

import com.recharge.phone.domain.model.recharge.Recharge;

public interface RechargeRepositoryPort {

    Recharge save(Recharge recharge);
    Optional<Recharge> findById(String id);
    List<Recharge> findByUserId(String userId, int page, int size);
    long countByUserId(String userId);

}
