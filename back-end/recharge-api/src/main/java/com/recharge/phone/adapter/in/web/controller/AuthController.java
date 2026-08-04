package com.recharge.phone.adapter.in.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.recharge.phone.adapter.in.web.AuthApi;
import com.recharge.phone.adapter.in.web.dto.LoginRequest;
import com.recharge.phone.adapter.in.web.dto.RefreshTokenRequest;
import com.recharge.phone.adapter.in.web.dto.TokenResponse;
import com.recharge.phone.application.port.in.AuthUseCase;
import com.recharge.phone.application.port.in.TokenResult;

@RestController
public class AuthController implements AuthApi {

    private static final String SAME_SITE_STRICT = "Strict";
    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @Override
    public ResponseEntity<TokenResponse> login(LoginRequest loginRequest) {
        TokenResult result = authUseCase.login(loginRequest.getEmail(), loginRequest.getPassword());
        return buildTokenResponse(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        TokenResult result = authUseCase.refreshToken(refreshTokenRequest.getRefreshToken());
        return buildTokenResponse(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logout() {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

    private ResponseEntity<TokenResponse> buildTokenResponse(TokenResult result, HttpStatus status) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", result.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path("/")
                .maxAge(result.expiresIn() / 1000)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite(SAME_SITE_STRICT)
                .path("/api/auth/refresh")
                .maxAge(604800)
                .build();

        TokenResponse body = new TokenResponse()
                .accessToken(result.accessToken())
                .refreshToken(result.refreshToken())
                .expiresIn(result.expiresIn());

        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }
}
