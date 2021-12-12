package com.example.demo.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;


class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp(){
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @Test
    @DisplayName("Should Validate Phone Number When It Is Valid")
    void itShouldValidatePhoneNumberWhenItIsValid(){

        String phoneNumber = "9999999999";

        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should Validate Phone Number Which Contains Invalid Characters")
    void itShouldValidatePhoneNumberWhenItContainsInvalidCharacters(){

        String phoneNumber = "982320000b";

        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        Assertions.assertThat(isValid).isFalse();
    }

}
