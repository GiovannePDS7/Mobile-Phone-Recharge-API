package com.recharge.phone.adapter.out.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.recharge.phone.application.event.CreateRechargeEvent;
import com.recharge.phone.application.port.out.KafkaRechargeEventPort;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class KafkaPublishRecharge implements KafkaRechargeEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "recharge-topic";

    @Override
    public void sendCreateRechargeEvent(CreateRechargeEvent event) {
        // Implementation for publishing recharge message to Kafka
        kafkaTemplate.send(TOPIC, event);
    }
}
