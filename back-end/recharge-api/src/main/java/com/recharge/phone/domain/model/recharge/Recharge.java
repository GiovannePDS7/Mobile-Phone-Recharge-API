package com.recharge.phone.domain.model.recharge;

import java.time.LocalDateTime;
import java.util.UUID;

public class Recharge {

  private String id;
  private String userId;
  private String phoneNumber;
  private double amount;
  private RechargeStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Recharge() {}

  // Creates a new recharge with PENDING status — ID and timestamps are domain concerns
  public static Recharge create(String userId, String phoneNumber, double amount) {
    var recharge = new Recharge();
    recharge.id = UUID.randomUUID().toString();
    recharge.userId = userId;
    recharge.phoneNumber = phoneNumber;
    recharge.amount = amount;
    recharge.status = RechargeStatus.PENDING;
    recharge.createdAt = LocalDateTime.now();
    recharge.updatedAt = recharge.createdAt;
    return recharge;
  }

  // Reconstitutes a Recharge from persistence — no side effects
  public static Recharge reconstitute(String id, String userId, String phoneNumber,
      double amount, RechargeStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
    var recharge = new Recharge();
    recharge.id = id;
    recharge.userId = userId;
    recharge.phoneNumber = phoneNumber;
    recharge.amount = amount;
    recharge.status = status;
    recharge.createdAt = createdAt;
    recharge.updatedAt = updatedAt;
    return recharge;
  }

  public void startProcessing() {
    this.status = RechargeStatus.PROCESSING;
    this.updatedAt = LocalDateTime.now();
  }

  public void complete() {
    this.status = RechargeStatus.COMPLETED;
    this.updatedAt = LocalDateTime.now();
  }

  public void fail() {
    this.status = RechargeStatus.FAILED;
    this.updatedAt = LocalDateTime.now();
  }

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public String getPhoneNumber() { return phoneNumber; }
  public double getAmount() { return amount; }
  public RechargeStatus getStatus() { return status; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
}
