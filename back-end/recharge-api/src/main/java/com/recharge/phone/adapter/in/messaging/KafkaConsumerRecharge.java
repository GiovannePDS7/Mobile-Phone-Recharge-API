package com.recharge.phone.adapter.in.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.recharge.phone.application.event.CreateRechargeEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaConsumerRecharge {
    
    private final String TOPIC = "recharge-topic";
    private final String GROUP_ID = "recharge-group";

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consumeRechargeEvent(CreateRechargeEvent rechargeEvent) {
        // Atualizar o valor do saldo do telefone com base no evento recebido
        log.info("Received recharge event: {}", rechargeEvent);

        // Atualizar o status da recarga para "COMPLETED" ou "FAILED" com base no resultado da operação
        log.info("Updating recharge status for phone number: {}", rechargeEvent.phoneNumber());
    }
}
