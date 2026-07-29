package com.recharge.phone.domain.model;

import java.util.UUID;

public class Recharge {
  private UUID id;
  private String phoneNumber;
  private double amount;
  private User user;

  public Recharge(UUID id, String phoneNumber, double amount) {
    this.id = id;
    this.phoneNumber = phoneNumber;
    this.amount = amount;
    this.user = null;
  }

  //Getters
  public UUID getId(){
    return id;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public double getAmount() {
    return amount;
  }

  public User getUser() {
    return user;
  }

  //Setters
  public void setId(UUID id) {
    this.id = id;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
