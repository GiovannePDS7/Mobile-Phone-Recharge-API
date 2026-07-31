package com.recharge.phone.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.recharge.phone.application.port.in.PhoneUseCase;
import com.recharge.phone.application.port.out.PhoneRepositoryPort;
import com.recharge.phone.domain.model.Phone;

@Service
public class PhoneService implements PhoneUseCase {

    private final PhoneRepositoryPort phoneRepository;

    public PhoneService(PhoneRepositoryPort phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone registerPhone(String phoneNumber, String label) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (phoneRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        Phone phone = new Phone(userId, phoneNumber, label, BigDecimal.ZERO);
        return phoneRepository.save(phone);
    }

    @Override
    public List<Phone> listPhones(String userId) {
        return phoneRepository.findByUserId(userId);
    }

    @Override
    public void deletePhone(String userId, String phoneId) {
        phoneRepository.findByIdAndUserId(phoneId, userId).orElseThrow(() -> new IllegalArgumentException("Phone not found"));

        phoneRepository.deleteById(phoneId);
    }
}
