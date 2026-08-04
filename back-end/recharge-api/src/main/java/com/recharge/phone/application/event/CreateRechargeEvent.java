package com.recharge.phone.application.event;

import java.util.UUID;

public record CreateRechargeEvent(UUID id, String userId, String phoneNumber, double amount) {
}
