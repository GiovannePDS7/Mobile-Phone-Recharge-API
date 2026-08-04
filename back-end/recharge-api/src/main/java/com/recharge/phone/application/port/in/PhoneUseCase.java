package com.recharge.phone.application.port.in;

import java.util.List;

import com.recharge.phone.domain.model.Phone;

public interface PhoneUseCase {

    Phone registerPhone(String phoneNumber, String label);

    List<Phone> listPhones(String userId);

    void deletePhone(String userId, String phoneId);

    Phone getPhoneByUserIdAndPhoneNumber(String userId, String phoneNumber);
}
