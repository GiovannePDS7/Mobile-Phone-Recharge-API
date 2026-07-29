package com.recharge.phone.application.port.in.rechargeImp;

import com.recharge.phone.application.dto.RechargePageResult;

public interface FindRechargeHistoryUseCase {
    RechargePageResult findHistory(String userId, int page, int size);
}
