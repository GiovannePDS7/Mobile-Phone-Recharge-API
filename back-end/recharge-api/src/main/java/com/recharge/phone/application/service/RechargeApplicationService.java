package com.recharge.phone.application.service;

import org.springframework.stereotype.Service;

import com.recharge.phone.application.port.out.RechargeRepositoryPort;
import com.recharge.phone.application.port.in.CreateRechargeUseCase;
import com.recharge.phone.application.event.CreateRechargeEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeApplicationService implements CreateRechargeUseCase {

  private final RechargeRepositoryPort rechargeRepositoryPort;

  @Override
  public void createRecharge(CreateRechargeEvent event) {
    rechargeRepositoryPort.createRecharge(event);
  }
}
