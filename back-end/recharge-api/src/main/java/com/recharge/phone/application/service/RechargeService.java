package com.recharge.phone.application.service;

import com.recharge.phone.application.event.CreateRechargeEvent;
import java.util.List;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeStatus;
import com.recharge.phone.domain.exception.AmountIsToLowException;
import com.recharge.phone.domain.exception.RechargeNotFoundException;
import com.recharge.phone.domain.exception.UnauthorizedException;
import com.recharge.phone.adapter.out.messaging.KafkaPublishRecharge;
import com.recharge.phone.application.port.in.RechargeUseCase;
import com.recharge.phone.application.port.out.RechargeRepositoryPort;
import com.recharge.phone.domain.model.recharge.Recharge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeService implements RechargeUseCase{

    private final RechargeRepositoryPort rechargeRepositoryPort;
    private final KafkaPublishRecharge kafkaPublishRecharge;

    @Override
    public RechargeResponse createRecharge(CreateRechargeRequest createRechargeRequest) {
        Recharge recharge = returnDomain(createRechargeRequest);
        kafkaPublishRecharge.sendCreateRechargeEvent(
            new CreateRechargeEvent(
                UUID.fromString(recharge.getId()),
                recharge.getUserId(),
                recharge.getPhoneNumber(),
                recharge.getAmount()));
        return returnDto(rechargeRepositoryPort.save(recharge));
    }

    @Override
    public RechargeResponse getRechargeById(String id) {
        Recharge recharge = rechargeRepositoryPort.findById(id)
                .orElseThrow(() -> new RechargeNotFoundException(id));
        return returnDto(recharge);
    }

    @Override
    public RechargePageResponse getRechargeHistory(Integer page, Integer size) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Usuário não autenticado");
        }
        String userId = auth.getName();

        List<RechargeResponse> rechargeResponses = rechargeRepositoryPort.findByUserId(userId, page, size)
                .stream()
                .map(this::returnDto)
                .toList();
        long total = rechargeRepositoryPort.countByUserId(userId);
        return new RechargePageResponse().content(rechargeResponses).page(page).size(size).totalElements(total).totalPages((int) Math.ceil((double) total / size));
    }

    public Recharge returnDomain(CreateRechargeRequest createRechargeRequest) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Usuário não autenticado");
        }
        String userId = (String) auth.getPrincipal();

        if (createRechargeRequest.getAmount() <= 0) {
            throw new AmountIsToLowException(String.valueOf(createRechargeRequest.getAmount()));
        }

        return Recharge.create(userId, createRechargeRequest.getPhoneNumber(),
                createRechargeRequest.getAmount());
    }

    public RechargeResponse returnDto(Recharge recharge) {
        RechargeResponse rechargeResponse = new RechargeResponse();
        rechargeResponse.setId(recharge.getId());
        rechargeResponse.setPhoneNumber(recharge.getPhoneNumber());
        rechargeResponse.setAmount(recharge.getAmount());
        rechargeResponse.setStatus(RechargeStatus.fromValue(recharge.getStatus().name()));
        rechargeResponse.setCreatedAt(recharge.getCreatedAt().atOffset(UTC));
        rechargeResponse.setUpdatedAt(recharge.getUpdatedAt().atOffset(UTC));
        return rechargeResponse;
    }
}

