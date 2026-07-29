package com.recharge.phone.aplication.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recharge.phone.aplication.port.in.AuthUseCase;
import com.recharge.phone.aplication.port.in.TokenResult;
import com.recharge.phone.aplication.port.out.UserRepositoryPort;
import com.recharge.phone.config.JwtService;
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return generateTokens(user);
    }

    @Override
    public TokenResult refreshToken(String refreshToken) {
        if(!jwtService.isTokenValid(refreshToken)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return generateTokens(user);
    }

    @Override
    public User register(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = new User(name, email, passwordHash);
        return userRepository.createUser(user);
    }

    private TokenResult generateTokens(User user) {
        String acessToken = jwtService.generateAcessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());
        return new TokenResult(acessToken, refreshToken, jwtService.getAccessTokenExpiration());
    }

}
