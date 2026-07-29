package com.recharge.phone.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.recharge.phone.domain.model.Recharge;
import com.recharge.phone.application.event.CreateRechargeEvent;

public interface RechargeRepositoryPort {

  void createRecharge(CreateRechargeEvent event);
  Optional<Recharge> findById(UUID id);
  Optional<Recharge> findByPhoneNumber(String phoneNumber);
  boolean existsByPhoneNumber(String phoneNumber);

}
