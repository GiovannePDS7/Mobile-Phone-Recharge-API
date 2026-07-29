package com.recharge.phone.domain.exception;

public class AmountIsToLowException extends RuntimeException {
  public AmountIsToLowException(String message) {
    super("O valor da recarga é muito baixo: " + message);
  }
}
