package com.recharge.phone.application.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recharge.phone.application.port.in.UserUseCase;
import com.recharge.phone.application.port.out.PhoneRepositoryPort;
import com.recharge.phone.application.port.out.UserRepositoryPort;
import com.recharge.phone.domain.model.Phone;
import com.recharge.phone.domain.model.User;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepository;
    private final PhoneRepositoryPort phoneRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepository, PhoneRepositoryPort phoneRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Phone> phones = phoneRepository.findByUserId(userId);
        user.setPhones(phones);
        return user;
    }

    @Override
    public User update(String userId, String name, String email, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (name != null) {
            user.setName(name);
        }
        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(email);
        }
        if (password != null) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }

        return userRepository.updateUser(user);
    }

    @Override
    public void delete(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        phoneRepository.deleteAllByUserId(userId);

        userRepository.deleteById(userId);
    }
}
