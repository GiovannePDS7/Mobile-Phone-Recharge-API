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

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new SecurityException("Usuário não autenticado");
        }
        String userId = auth.getName();

        if (phoneRepository.existsByUserIdAndPhoneNumber(userId, phoneNumber)) {
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
    public Phone getPhoneByUserIdAndPhoneNumber(String userId, String phoneNumber) {
        return phoneRepository.findByUserIdAndPhoneNumber(userId, phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Phone not found"));
    }

    @Override
    public void deletePhone(String userId, String phoneId) {
        phoneRepository.findByIdAndUserId(phoneId, userId).orElseThrow(() -> new IllegalArgumentException("Phone not found"));

        phoneRepository.deleteById(phoneId);
    }
}
