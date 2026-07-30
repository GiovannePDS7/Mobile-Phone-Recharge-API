package com.recharge.phone.adapter.in.web.controller;

import org.springframework.http.ResponseEntity;

import com.recharge.phone.adapter.in.web.RechargesApi;
import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.application.port.in.RechargeUseCase;
import com.recharge.phone.application.service.RechargeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RechargerController implements RechargesApi{

    private final RechargeUseCase rechargeUseCase;

    public RechargerController(RechargeUseCase rechargeUseCase) {
        this.rechargeUseCase = rechargeUseCase;
    }

    @Override   
    public ResponseEntity<RechargeResponse> createRecharge(@Valid CreateRechargeRequest createRechargeRequest) {
        // TODO Auto-generated method stub
        RechargeResponse rechargeResponse = rechargeUseCase.createRecharge(createRechargeRequest);
        return ResponseEntity.accepted().body(rechargeResponse);
    }

    @Override
    public ResponseEntity<RechargeResponse> getRechargeById(String id) {
        // TODO Auto-generated method stub
        
        return null;
    }

    @Override
    public ResponseEntity<RechargePageResponse> getRechargeHistory(@Min(0) @Valid Integer page,
            @Min(1) @Max(100) @Valid Integer size) {
        // TODO Auto-generated method stub
        return null;
    }
}
