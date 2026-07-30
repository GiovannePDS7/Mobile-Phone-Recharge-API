package com.recharge.phone.application.port.out;

import com.recharge.phone.application.event.CreateRechargeEvent;

public interface KafkaRechargeEventPort {
  void sendCreateRechargeEvent(CreateRechargeEvent event);
}
