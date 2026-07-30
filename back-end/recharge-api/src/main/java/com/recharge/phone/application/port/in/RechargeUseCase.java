package com.recharge.phone.application.port.in;

import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;

public interface RechargeUseCase {

    RechargeResponse createRecharge(CreateRechargeRequest createRechargeRequest);

    RechargeResponse getRechargeById(String id);

    RechargePageResponse getRechargeHistory(Integer page, Integer size);  
}