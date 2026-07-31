package com.recharge.phone.application.service;

import com.recharge.phone.application.event.CreateRechargeEvent;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.recharge.phone.adapter.in.web.dto.CreateRechargeRequest;
import com.recharge.phone.adapter.in.web.dto.RechargePageResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeResponse;
import com.recharge.phone.adapter.in.web.dto.RechargeStatus;
import com.recharge.phone.adapter.out.messaging.KafkaPublishRecharge;
import com.recharge.phone.application.port.in.RechargeUseCase;
import com.recharge.phone.application.port.out.RechargeRepositoryPort;
import com.recharge.phone.domain.model.recharge.Recharge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeService implements RechargeUseCase {

    private final RechargeRepositoryPort rechargeRepositoryPort;
    private final KafkaPublishRecharge kafkaPublishRecharge;

    @Override
    public RechargeResponse createRecharge(CreateRechargeRequest createRechargeRequest) {
        Recharge recharge = returnDomain(createRechargeRequest);
        kafkaPublishRecharge.sendCreateRechargeEvent(
            new CreateRechargeEvent(
                UUID.fromString(recharge.getId()),
                recharge.getPhoneNumber(),
                recharge.getAmount()));
        return returnDto(rechargeRepositoryPort.save(recharge));
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

    public Recharge returnDomain(CreateRechargeRequest createRechargeRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Recharge recharge = Recharge.create(userId, createRechargeRequest.getPhoneNumber(),
                createRechargeRequest.getAmount());
        return recharge;
    }

    public RechargeResponse returnDto(Recharge recharge) {
        RechargeResponse rechargeResponse = new RechargeResponse();
        rechargeResponse.setId(recharge.getId());
        rechargeResponse.setPhoneNumber(recharge.getPhoneNumber());
        rechargeResponse.setAmount(recharge.getAmount());
        rechargeResponse.setStatus(RechargeStatus.PENDING);
        rechargeResponse.setCreatedAt(OffsetDateTime.now(UTC));
        rechargeResponse.setUpdatedAt(OffsetDateTime.now(UTC));
        return rechargeResponse;
    }
}
