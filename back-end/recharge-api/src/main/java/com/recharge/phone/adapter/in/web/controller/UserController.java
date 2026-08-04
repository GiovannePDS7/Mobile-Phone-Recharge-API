package com.recharge.phone.adapter.in.web.controller;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.recharge.phone.adapter.in.web.UsersApi;
import com.recharge.phone.adapter.in.web.dto.PhoneResponse;
import com.recharge.phone.adapter.in.web.dto.RegisterRequest;
import com.recharge.phone.adapter.in.web.dto.UpdateUserRequest;
import com.recharge.phone.adapter.in.web.dto.UserProfileResponse;
import com.recharge.phone.adapter.in.web.dto.UserResponse;
import com.recharge.phone.application.port.in.UserUseCase;
import com.recharge.phone.domain.exception.UnauthorizedException;
import com.recharge.phone.domain.model.User;

@RestController
public class UserController implements UsersApi {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @Override
    public ResponseEntity<UserResponse> registerUser(RegisterRequest registerRequest) {
        User user = userUseCase.register(
                registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        UserResponse response = new UserResponse()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<UserProfileResponse> getCurrentUser() {
        String userId = getCurrentUserId();
        User user = userUseCase.getProfile(userId);

        List<PhoneResponse> phones = user.getPhones().stream()
                .map(p -> new PhoneResponse()
                        .id(p.getId())
                        .phoneNumber(p.getPhoneNumber())
                        .label(p.getLabel())
                        .amount(p.getAmount() != null ? p.getAmount().doubleValue() : null)
                        .createdAt(OffsetDateTime.ofInstant(p.getCreatedAt(), ZoneOffset.UTC)))
                .toList();

        UserProfileResponse response = new UserProfileResponse()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phones(phones);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateCurrentUser(UpdateUserRequest request) {
        String userId = getCurrentUserId();
        User user = userUseCase.update(userId, request.getName(), request.getEmail(), request.getPassword());

        UserResponse response = new UserResponse()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {
        String userId = getCurrentUserId();
        userUseCase.delete(userId);
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Usuário não autenticado");
        }
        return (String) auth.getPrincipal();
    }
}
