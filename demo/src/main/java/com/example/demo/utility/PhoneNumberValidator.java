package com.example.demo.utility;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberValidator {
    public boolean validate(String phoneNumber) {

        // TODO remove white spaces, use lombok validation istead
        if(phoneNumber.length() != 10){
            return false;
        }

        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!(phoneNumber.charAt(i) >= '0'
                    && phoneNumber.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;

    }
}
