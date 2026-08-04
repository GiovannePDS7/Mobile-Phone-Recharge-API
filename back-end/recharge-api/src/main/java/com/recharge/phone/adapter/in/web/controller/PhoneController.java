package com.recharge.phone.adapter.in.web.controller;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.recharge.phone.adapter.in.web.PhonesApi;
import com.recharge.phone.adapter.in.web.dto.PhoneResponse;
import com.recharge.phone.adapter.in.web.dto.RegisterPhoneRequest;
import com.recharge.phone.application.port.in.PhoneUseCase;
import com.recharge.phone.domain.model.Phone;

@RestController
public class PhoneController implements PhonesApi {

    private final PhoneUseCase phoneUseCase;

    public PhoneController(PhoneUseCase phoneUseCase) {
        this.phoneUseCase = phoneUseCase;
    }

    @Override
    public ResponseEntity<PhoneResponse> registerPhone(RegisterPhoneRequest request) {
        Phone phone = phoneUseCase.registerPhone(request.getPhoneNumber(), request.getLabel());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(phone));
    }

    @Override
    public ResponseEntity<List<PhoneResponse>> listPhones() {
        String userId = getCurrentUserId();
        List<PhoneResponse> phones = phoneUseCase.listPhones(userId).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(phones);
    }

    @Override
    public ResponseEntity<Void> deletePhone(String id) {
        String userId = getCurrentUserId();
        phoneUseCase.deletePhone(userId, id);
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new SecurityException("Usuário não autenticado");
        }
        return (String) auth.getPrincipal();
    }

    private PhoneResponse toResponse(Phone phone) {
        return new PhoneResponse()
                .id(phone.getId())
                .phoneNumber(phone.getPhoneNumber())
                .label(phone.getLabel())
                .amount(phone.getAmount() != null ? phone.getAmount().doubleValue() : null)
                .createdAt(OffsetDateTime.ofInstant(phone.getCreatedAt(), ZoneOffset.UTC));
    }
}
