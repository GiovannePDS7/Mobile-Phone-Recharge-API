package com.recharge.phone.domain.model;

import java.time.Instant;

public class Phone {
    private String id;
    private String userId;
    private String phoneNumber;
    private String label;
    private Instant createdAt;

    public Phone(String userId, String phoneNumber, String label) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.label = label;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getLabel() { return label; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
