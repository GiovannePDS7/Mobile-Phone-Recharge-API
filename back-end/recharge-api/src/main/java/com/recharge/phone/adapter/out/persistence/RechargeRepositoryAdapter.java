package com.recharge.phone.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.recharge.phone.application.port.out.RechargeRepositoryPort;
import com.recharge.phone.domain.model.recharge.Recharge;
import com.recharge.phone.domain.model.recharge.RechargeStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RechargeRepositoryAdapter implements RechargeRepositoryPort {

    private final RechargeMongoRepository mongoRepository;

    @Override
    public Recharge save(Recharge recharge) {
        var saved = mongoRepository.save(toDocument(recharge));
        return toDomain(saved);
    }

    @Override
    public Optional<Recharge> findById(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Recharge> findByUserId(String userId, int page, int size) {
        return mongoRepository
            .findByUserId(userId, PageRequest.of(page, size))
            .map(this::toDomain)
            .toList();
    }

    @Override
    public long countByUserId(String userId) {
        return mongoRepository.countByUserId(userId);
    }

    @Override
    public void updateRechargeStatus(String rechargeId, RechargeStatus status) {
        Optional<RechargeDocument> optionalRecharge = mongoRepository.findById(rechargeId);
        
        if (optionalRecharge.isPresent()) {
            RechargeDocument rechargeDocument = optionalRecharge.get();
            rechargeDocument.setStatus(status);
            mongoRepository.save(rechargeDocument);
        } else {
            throw new IllegalArgumentException("Recharge not found with id: " + rechargeId);
        }
    }
    

    private RechargeDocument toDocument(Recharge r) {
        return new RechargeDocument(
            r.getId(),
            r.getUserId(),
            r.getPhoneNumber(),
            r.getAmount(),
            r.getStatus(),
            r.getCreatedAt(),
            r.getUpdatedAt());
    }

    private Recharge toDomain(RechargeDocument doc) {
        return Recharge.reconstitute(
            doc.getId(),
            doc.getUserId(),
            doc.getPhoneNumber(),
            doc.getAmount(),
            doc.getStatus(),
            doc.getCreatedAt(),
            doc.getUpdatedAt());
    }
}
