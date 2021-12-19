package com.example.project.servicetest;

import com.example.project.user.model.User;
import com.example.project.user.dto.UserDTO;
import com.example.project.user.repository.UserRepository;
import com.example.project.user.service.EmailService;
import com.example.project.user.service.UserService;
import com.example.project.utility.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @Mock
    private EmailService emailService;

    private UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService(userRepository, phoneNumberValidator,emailService);
    }

    @Test
    void itShouldVerifyOtpWhenCorrect(){

        boolean result = userService.verifyOtp(100,100);

        Assertions.assertThat(result).isTrue();

    }

    @Test
    void itShouldVerifyOtpWhenIncorrect(){

        boolean result = userService.verifyOtp(105,100);

        Assertions.assertThat(result).isFalse();

    }

    @Test
    void itShouldActivateUserWhenUserIdMatches(){

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user));

        userService.activateUser(user.getId());

        Assertions.assertThat(user.getStatus()).isTrue();
    }

    @Test
    void itShouldDeactivateUserWhenUserIdMatches(){

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user));

        userService.deactivateUser(user.getId());

        Assertions.assertThat(user.getStatus()).isFalse();
    }

    @Test
    void itShouldSendVerificationEmailIfUserIdMatches(){

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.sendVerificationEmailToTheUser(user.getId());

        Assertions.assertThat(user.getOtp()).isEqualTo(123456);
    }

    @Test
    void itShouldAddNewUserIfPhoneNumberIsValid(){

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName("Rahul123q");
        userDTO.setEmail("rahul123@gmail.com");
        userDTO.setFirstAndLastName("Rahul Shinde");
        userDTO.setPhoneNumber("9999999999");

        Mockito.when(phoneNumberValidator.validate(userDTO.getPhoneNumber())).thenReturn(true);

        User user2 = userService.addNewUser(userDTO);

        Assertions.assertThat(user2.getUserName()).isEqualTo(userDTO.getUserName());

    }

    @Test
    void itShouldNotAddNewUserIfPhoneNumberIsInvalid(){

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName("Rahul123q");
        userDTO.setEmail("rahul123@gmail.com");
        userDTO.setFirstAndLastName("Rahul Shinde");
        userDTO.setPhoneNumber("9999999999");

        Mockito.when(phoneNumberValidator.validate(userDTO.getPhoneNumber())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
                    User user2 = userService.addNewUser(userDTO);
                })
                .hasMessage("Invalid Phone Number Entered.");

    }

    @Test
    void itShouldThrowExceptionIfUsernameAlreadyInUse(){

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName("Rahul123q");
        userDTO.setEmail("rahul123@gmail.com");
        userDTO.setFirstAndLastName("Rahul Shinde");
        userDTO.setPhoneNumber("9999999999");

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> {
                    User user2 = userService.addNewUser(userDTO);
        })
                .hasMessage("UserName is not available.");
    }

    @Test
    void itShouldThrowExceptionIfEmailAlreadyInUse(){

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName("Rahul123q");
        userDTO.setEmail("rahul123@gmail.com");
        userDTO.setFirstAndLastName("Rahul Shinde");
        userDTO.setPhoneNumber("9999999999");

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> {
                    User user2 = userService.addNewUser(userDTO);
                })
                .hasMessage("Email is already registered by other user.");

    }

    @Test
    void itShouldUpdateUserDetails(){

        UserDTO userDTO = new UserDTO();

        userDTO.setUserName("Rahul123q");
        userDTO.setEmail("rahul@gmail.com");
        userDTO.setFirstAndLastName("Rahul Shinde");
        userDTO.setPhoneNumber("9999999999");

        User user = new User(2,"Rahul12q","rahul123@gmail.com","1234567890","Rahul Shinde",false,0);

        Mockito.when(phoneNumberValidator.validate(userDTO.getPhoneNumber())).thenReturn(true);

        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user));

        userService.updateUser(user.getId(),userDTO);

        Assertions.assertThat(user.getUserName()).isEqualTo(userDTO.getUserName());
        Assertions.assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());

    }
}
