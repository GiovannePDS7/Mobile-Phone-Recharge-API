package com.recharge.phone.application.port.in.rechargeImp;

import com.recharge.phone.application.dto.CreateRechargeCommand;
import com.recharge.phone.application.dto.RechargeResultCommand;

public interface CreateRechargeUseCase {
    RechargeResultCommand createRecharge(CreateRechargeCommand event);
}