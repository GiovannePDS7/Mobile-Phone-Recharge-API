package com.recharge.phone.application.port.in.rechargeImp;

import com.recharge.phone.application.dto.RechargeResultCommand;

public interface FindRechargeUseCase {
    RechargeResultCommand findRechargeById(String id);
}
