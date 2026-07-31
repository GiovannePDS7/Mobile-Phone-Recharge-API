package com.recharge.phone.adapter.out.persistence;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.recharge.phone.domain.model.recharge.RechargeStatus;

@Document(collection = "recharges")
public class RechargeDocument {

    @Id
    private String id;
    private String userId;
    private String phoneNumber;
    private double amount;
    private RechargeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RechargeDocument() {}

    public RechargeDocument(String id, String userId, String phoneNumber,
            double amount, RechargeStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
      return id;
    }
    public String getUserId() {
      return userId;
    }
    public String getPhoneNumber() {
      return phoneNumber;
    }
    public double getAmount() {
      return amount;
    }
    public RechargeStatus getStatus() {
      return status;
    }
    public void setStatus(RechargeStatus status) {
      this.status = status;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
      return updatedAt;
    }
}
