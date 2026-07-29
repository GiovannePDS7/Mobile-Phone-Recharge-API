package com.recharge.phone.application.port.in;

import com.recharge.phone.application.event.CreateRechargeEvent;

public interface CreateRechargeUseCase {
    void createRecharge(CreateRechargeEvent event);
}