package com.recharge.phone.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.recharge.phone.application.port.out.PhoneRepositoryPort;
import com.recharge.phone.domain.model.Phone;

@Repository
public class PhoneRepositoryAdapter implements PhoneRepositoryPort {

    private final SpringDataPhoneRepository repository;

    public PhoneRepositoryAdapter(SpringDataPhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public Phone save(Phone phone) {
        PhoneDocument doc = new PhoneDocument(
                phone.getUserId(),
                phone.getPhoneNumber(),
                phone.getLabel(),
                phone.getAmount(),
                phone.getCreatedAt()
        );
        PhoneDocument saved = repository.save(doc);
        phone.setId(saved.getId());
        return phone;
    }

    @Override
    public List<Phone> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Phone> findByIdAndUserId(String id, String userId) {
        return repository.findById(id)
                .filter(doc -> doc.getUserId().equals(userId))
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber) {
        return repository.existsByUserIdAndPhoneNumber(userId, phoneNumber);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        repository.deleteAllByUserId(userId);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber);
    }

    private Phone toDomain(PhoneDocument doc) {
        Phone phone = new Phone(doc.getUserId(), doc.getPhoneNumber(), doc.getLabel(), doc.getAmount());
        phone.setId(doc.getId());
        phone.setCreatedAt(doc.getCreatedAt());
        return phone;
    }
}
