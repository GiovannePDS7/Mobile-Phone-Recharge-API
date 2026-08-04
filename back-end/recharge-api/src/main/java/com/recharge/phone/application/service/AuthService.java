package com.recharge.phone.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recharge.phone.application.port.in.AuthUseCase;
import com.recharge.phone.application.port.in.TokenResult;
import com.recharge.phone.application.port.out.UserRepositoryPort;
import com.recharge.phone.config.JwtService;
import com.recharge.phone.domain.exception.InvalidCredentialsException;
import com.recharge.phone.domain.exception.InvalidTokenException;
import com.recharge.phone.domain.model.User;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResult login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return generateTokens(user);
    }

    @Override
    public TokenResult refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException();
        }

        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidTokenException::new);

        return generateTokens(user);
    }

    private TokenResult generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());
        return new TokenResult(accessToken, refreshToken, jwtService.getAccessTokenExpiration());
    }
}
