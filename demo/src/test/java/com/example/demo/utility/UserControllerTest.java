package com.example.demo.utility;


import com.example.demo.user.User;
import com.example.demo.user.UserController;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.assertj.core.api.Assertions;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private static UserController userController;

    @BeforeEach
    void setUp(){

        userService = new UserService(userRepository);

        userController = new UserController(userService);
    }

    @Test
    public void shouldRegisterNewUser(){

//        User user = new User();
//
//        user.setUserName("userName");
//        user.setFirstAndLastName("name");
//        user.setEmail("email");
//        user.setPhoneNumber("1234567890");
//
//
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
//
//        Mockito.when(userService.addNewUser(user.getUserName(),user.getFirstAndLastName(),user.getEmail(),user.getPhoneNumber())).thenReturn(true);
//
//        boolean userRegisterTest = userController.registerNewUser("Radful","asura","rahsjamail.com","9999998999");

        //Assertions.assertThat(userRegisterTest).isTrue();
    }

}
