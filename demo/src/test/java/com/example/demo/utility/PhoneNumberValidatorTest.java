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
    void itShouldValidatePhoneNumber(){
        //TODO name should be descriptive enough to understand the test case
        String phoneNumber = "9999999999";

        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        Assertions.assertThat(isValid).isTrue();

    }

    @Test
    @DisplayName("Phone Number Has Invalid Amount of Digits")
    void itShouldValidatePhoneNumberWhenNumberOfDigitsGreaterThanTen(){
        //TODO name can be improvised,
        // Mostly this kind of validation should be handled at UI side so this can be removed
        String phoneNumber = "99999999995";

        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        Assertions.assertThat(isValid).isFalse();

    }

    @Test
    @DisplayName("Phone Number Contains Characters")
    void itShouldValidatePhoneNumberWhenItContainsCharacters(){
        //TODO name can be improvised
        String phoneNumber = "982320000b";

        boolean isValid = phoneNumberValidator.validate(phoneNumber);

        Assertions.assertThat(isValid).isFalse();

    }

}
