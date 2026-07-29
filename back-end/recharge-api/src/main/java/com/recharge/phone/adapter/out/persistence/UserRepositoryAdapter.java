package com.recharge.phone.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.recharge.phone.application.port.out.UserRepositoryPort;
import com.recharge.phone.domain.model.User;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(User user) {
        UserDocument doc = new UserDocument(user.getName(), user.getEmail(), user.getPasswordHash());
        UserDocument saved = repository.save(doc);
        user.setId(saved.getId());
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(this::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        UserDocument doc = repository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        doc.setName(user.getName());
        doc.setEmail(user.getEmail());
        doc.setPasswordHash(user.getPasswordHash());
        repository.save(doc);
        return user;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    private User toUser(UserDocument doc) {
        User user = new User(doc.getName(), doc.getEmail(), doc.getPasswordHash());
        user.setId(doc.getId());
        return user;
    }
}
