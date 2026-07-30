package com.recharge.phone.application.service;

import org.springframework.stereotype.Service;

import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.application.port.in.RechargeUseCase;
import com.recharge.phone.application.port.out.RechargeRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeService implements RechargeUseCase{

    private final RechargeRepositoryPort rechargeRepositoryPort;

    @Override
    public RechargeResponse createRecharge(CreateRechargeRequest createRechargeRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RechargeResponse getRechargeById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RechargePageResponse getRechargeHistory(Integer page, Integer size) {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}

