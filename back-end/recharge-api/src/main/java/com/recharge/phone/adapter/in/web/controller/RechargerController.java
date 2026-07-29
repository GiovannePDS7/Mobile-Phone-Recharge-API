package com.recharge.phone.adapter.in.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.application.dto.CreateRechargeCommand;
import com.recharge.phone.application.dto.RechargeResultCommand;
import com.recharge.phone.application.port.in.rechargeImp.CreateRechargeUseCase;
import com.recharge.phone.application.port.in.rechargeImp.FindRechargeHistoryUseCase;
import com.recharge.phone.application.port.in.rechargeImp.FindRechargeUseCase;

@RestController
@RequestMapping("/api/recharges")
@RequiredArgsConstructor
public class RechargerController {

    private final CreateRechargeUseCase createRechargeUseCase;
    private final FindRechargeUseCase findRechargeUseCase;
    private final FindRechargeHistoryUseCase findRechargeHistoryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RechargeResponse createRecharge(@Valid @RequestBody CreateRechargeRequest request) {
        var result = createRechargeUseCase.createRecharge(
            new CreateRechargeCommand(request.userId(), request.phoneNumber(), request.amount()));
        return toResponse(result);
    }

    @GetMapping("/{id}")
    public RechargeResponse getById(@PathVariable String id) {
        return toResponse(findRechargeUseCase.findRechargeById(id));
    }

    @GetMapping
    public RechargePageResponse getHistory(
            // userId será substituído pelo principal do JWT no Passo de Auth
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        var result = findRechargeHistoryUseCase.findHistory(userId, page, size);

        var content = result.content().stream().map(this::toResponse).toList();

        return new RechargePageResponse(content, result.page(), result.size(),
            result.totalElements(), result.totalPages());
    }

    private RechargeResponse toResponse(RechargeResultCommand r) {
        return new RechargeResponse(r.id(), r.userId(), r.phoneNumber(),
            r.amount(), r.status(), r.createdAt(), r.updatedAt());
    }
}
