package com.recharge.phone.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recharge.phone.application.dto.CreateRechargeCommand;
import com.recharge.phone.application.dto.RechargePageResult;
import com.recharge.phone.application.dto.RechargeResultCommand;
import com.recharge.phone.application.port.in.rechargeImp.CreateRechargeUseCase;
import com.recharge.phone.application.port.in.rechargeImp.FindRechargeHistoryUseCase;
import com.recharge.phone.application.port.in.rechargeImp.FindRechargeUseCase;
import com.recharge.phone.application.port.out.RechargeRepositoryPort;
import com.recharge.phone.domain.exception.RechargeNotFoundException;
import com.recharge.phone.domain.model.recharge.Recharge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeApplicationService
    implements CreateRechargeUseCase, FindRechargeUseCase, FindRechargeHistoryUseCase {

    private final RechargeRepositoryPort rechargeRepositoryPort;

    @Override
    @Transactional
    public RechargeResultCommand createRecharge(CreateRechargeCommand command) {
        var recharge = Recharge.create(command.userId(), command.phoneNumber(), command.amount());
        var saved = rechargeRepositoryPort.save(recharge);
        return toResult(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RechargeResultCommand findRechargeById(String id) {
        return rechargeRepositoryPort.findById(id)
            .map(this::toResult)
            .orElseThrow(() -> new RechargeNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RechargePageResult findHistory(String userId, int page, int size) {
        List<RechargeResultCommand> content = rechargeRepositoryPort
            .findByUserId(userId, page, size)
            .stream()
            .map(this::toResult)
            .toList();

        long total = rechargeRepositoryPort.countByUserId(userId);
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);

        return new RechargePageResult(content, page, size, total, totalPages);
    }

    private RechargeResultCommand toResult(Recharge r) {
        return new RechargeResultCommand(
            r.getId(),
            r.getUserId(),
            r.getPhoneNumber(),
            r.getAmount(),
            r.getStatus().name(),
            r.getCreatedAt(),
            r.getUpdatedAt());
    }
}

