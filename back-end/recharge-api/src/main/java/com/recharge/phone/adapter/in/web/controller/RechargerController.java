package com.recharge.phone.adapter.in.web.controller;

import org.springframework.http.ResponseEntity;

import com.recharge.phone.adapter.in.web.RechargesApi;
import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.application.port.in.RechargeUseCase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RechargerController implements RechargesApi{

    private final RechargeUseCase rechargeUseCase;

    public RechargerController(RechargeUseCase rechargeUseCase) {
        this.rechargeUseCase = rechargeUseCase;
    }

    @Override   
    public ResponseEntity<RechargeResponse> createRecharge(@Valid CreateRechargeRequest createRechargeRequest) {
        RechargeResponse rechargeResponse = rechargeUseCase.createRecharge(createRechargeRequest);
        return ResponseEntity.accepted().body(rechargeResponse);
    }

    @Override
    public ResponseEntity<RechargeResponse> getRechargeById(String id) {
        return ResponseEntity.ok(rechargeUseCase.getRechargeById(id));
    }

    @Override
    public ResponseEntity<RechargePageResponse> getRechargeHistory(@Min(0) @Valid Integer page,
            @Min(1) @Max(100) @Valid Integer size) {
        ResponseEntity<RechargePageResponse> rechargePageResponse = ResponseEntity.ok(rechargeUseCase.getRechargeHistory(page, size));
        return rechargePageResponse;
    }
}
