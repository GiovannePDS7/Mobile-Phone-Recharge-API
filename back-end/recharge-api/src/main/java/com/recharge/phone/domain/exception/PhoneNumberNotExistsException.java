package com.recharge.phone.domain.exception;

public class PhoneNumberNotExistsException extends RuntimeException {
  public PhoneNumberNotExistsException(String message) {
    super("Esse número de telefone não existe: " + message);
  }
}
