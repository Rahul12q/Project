package com.example.demo.user;

import com.example.demo.utility.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/demo")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path="/register")
    public boolean registerNewUser(@RequestParam String userName ,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phoneNumber){

        boolean isRegistered = userService.addNewUser(userName,name,email,phoneNumber);

        if(isRegistered){
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException(
                    "A problem occurred while loading the user details"
            ));

            userService.DeactivateUser(userService,user);

            Integer otp = userService.sendVerificationEmailToTheUser(user);

            userService.updateUser(user.getId(),null,null,null,null,null, String.valueOf(otp));

            return true;
        }

        return false;

    }

    @PostMapping(path="/activate/{userId}")
    public void activateUser(@PathVariable("userId") Integer userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));
        userService.ActivateUser(userService,user);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Integer userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String status) {
        userService.updateUser(userId,userName,name,email,phoneNumber,status,null);
    }


    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUser(userId);
    }

}