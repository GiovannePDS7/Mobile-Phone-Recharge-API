package com.recharge.phone.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Phone {
    private String id;
    private String userId;
    private String phoneNumber;
    private String label;
    private BigDecimal amount;
    private Instant createdAt;

    public Phone(String userId, String phoneNumber, String label) {
        this(userId, phoneNumber, label, BigDecimal.ZERO);
    }

    public Phone(String userId, String phoneNumber, String label, BigDecimal amount) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.label = label;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getLabel() { return label; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
