package com.recharge.phone.adapter.out.persistence;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "phones")
@CompoundIndex(name = "user_phone_unique", def = "{'userId': 1, 'phoneNumber': 1}", unique = true)
public class PhoneDocument {

    @Id
    private String id;
    private String userId;
    private String phoneNumber;
    private String label;
    private Instant createdAt;

    public PhoneDocument() {}

    public PhoneDocument(String userId, String phoneNumber, String label, Instant createdAt) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.label = label;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
