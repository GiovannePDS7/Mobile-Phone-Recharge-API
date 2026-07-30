package com.recharge.phone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {
  @Bean
  public ProducerFactory<Object, String> producerFactory(KafkaProperties kafkaProperties) {
    // Configure and return the ProducerFactory
    return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
  }

  @Bean
  public KafkaTemplate<Object, String> kafkaTemplate(ProducerFactory<Object, String> producerFactory) {
    // Create and return the KafkaTemplate
    return new KafkaTemplate<>(producerFactory);
  }
}
