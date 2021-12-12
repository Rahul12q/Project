package com.example.demo.user;

import com.example.demo.utility.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/users")

public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path="/register")
    public String registerNewUser(@RequestBody UserDTO userDTO){

        userService.addNewUser(userDTO);

       User user = userService.findUserByUserName(userDTO.getUserName());

        if(user != null){
            userService.deactivateUser(user.getId());

            userService.sendVerificationEmailToTheUser(user.getId());

            return "User Registered Successfully";
        }
        else{
            return "User Registration Failed";
        }
    }

    @PostMapping(path="/activate/{userId}")
    public void activateUser(@PathVariable("userId") Integer userId){

        userService.activateUser(userId);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Integer userId,
            @RequestBody(required = false) UserDTO userDTO) {
        userService.updateUser(userId,userDTO);
    }


    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUser(userId);
    }

}