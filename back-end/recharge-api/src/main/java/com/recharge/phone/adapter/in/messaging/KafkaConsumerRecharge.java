package com.recharge.phone.adapter.in.messaging;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.recharge.phone.adapter.out.persistence.PhoneRepositoryAdapter;
import com.recharge.phone.adapter.out.persistence.RechargeRepositoryAdapter;
import com.recharge.phone.application.event.CreateRechargeEvent;
import com.recharge.phone.domain.model.Phone;
import com.recharge.phone.domain.model.recharge.RechargeStatus;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerRecharge {

    private final String TOPIC = "recharge-topic";
    private final String GROUP_ID = "recharge-group";
    private final PhoneRepositoryAdapter phoneRepositoryAdapter;
    private final RechargeRepositoryAdapter rechargeRepositoryAdapter;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consumeRechargeEvent(CreateRechargeEvent rechargeEvent) {
        
        String rechargeId = rechargeEvent.id().toString();

        try{
            rechargeRepositoryAdapter.updateRechargeStatus(rechargeId, RechargeStatus.PROCESSING);
            log.info("Processing recharge event for userId: {}, phoneNumber: {}", rechargeEvent.userId(), rechargeEvent.phoneNumber());

            Phone phone = phoneRepositoryAdapter.findByUserIdAndPhoneNumber(rechargeEvent.userId(), rechargeEvent.phoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Phone not found"));

            phone.setAmount(phone.getAmount().add(BigDecimal.valueOf(rechargeEvent.amount())));
            phoneRepositoryAdapter.save(phone);

            rechargeRepositoryAdapter.updateRechargeStatus(rechargeId, RechargeStatus.COMPLETED);
            log.info("Recharge event processed successfully for userId: {}, phoneNumber: {}", rechargeEvent.userId(), rechargeEvent.phoneNumber());

        } catch (Exception e) {
            log.error("Error processing recharge event: {}", e.getMessage());
            rechargeRepositoryAdapter.updateRechargeStatus(rechargeId, RechargeStatus.FAILED);
        }
    }
}
