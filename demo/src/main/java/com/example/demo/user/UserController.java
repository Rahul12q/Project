package com.example.demo.user;

import com.example.demo.utility.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/demo") // TODO : path name should sugeest the resource that we want to access
                                // eg. users/
public class UserController {

    @Autowired // TODO : Why do we need to inject repo in controller, this needs to be injected in service
    private UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;
    // TODO One thing to understand about validation is if it's business validation handle it in service

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path="/all") // no need to mention all if /user is used as path get represnts getting all user
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(path="/register") // change this to accept request body create DTO to accept it
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
        //TODO: all business logic needs to be handled in service
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));
        userService.ActivateUser(userService,user);
    }

    @PutMapping(path = "{userId}")
    //TODO replace all pathvariable except userId to request body, use dto
    public void updateUser(
            @PathVariable("userId") Integer userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String status) { // status is not supposed to be updated from user
        userService.updateUser(userId,userName,name,email,phoneNumber,status,null);
    }


    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUser(userId);
    }

}