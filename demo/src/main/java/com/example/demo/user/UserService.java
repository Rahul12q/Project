package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.utility.PhoneNumberValidator;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean addNewUser(String userName,String name,String email,String phoneNumber) {
        User user = new User();

        user.setUserName(userName);
        user.setFirstAndLastName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        Optional<User> userOptionalEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> userOptionalUserName = userRepository.findByUserName(user.getUserName());

        if(userOptionalUserName.isPresent()){
            System.out.println("UserName is not available.");
            return false;
        }

        if(userOptionalEmail.isPresent()){
            System.out.println("Email is already registered by other user.");
            return false;
        }

        boolean isValid = phoneNumberValidator.validate(user.getPhoneNumber());

        if(!isValid) {

            System.out.println("Invalid Phone Number Entered.");
            return false;
        }
        userRepository.save(user);

        return true;

    }

    @Transactional
    public void updateUser(Integer userId,String userName, String name, String email, String phoneNumber, String status, String otp) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));

        Optional<User> userOptionalUserName = userRepository.findByUserName(userName);

        if(userName != null && userName.length() > 0) {
            if (userOptionalUserName.isPresent()) {
                throw new IllegalStateException("UserName is not available.");
            } else {
                user.setUserName(userName);
            }
        }

        if(name != null && (name.length() > 0)){
            user.setFirstAndLastName(name);
        }

        if(email != null && email.length() > 0){
            Optional<User> userOptional = userRepository.findByEmail(email);
            if(userOptional.isPresent()){
                throw new IllegalStateException("Old Email Address Entered");
            }
            user.setEmail(email);
        }

        boolean isValid = phoneNumberValidator.validate(user.getPhoneNumber());

        if(!isValid) {
            throw new IllegalStateException("Invalid Phone Number Entered.");
        }else{
            user.setPhoneNumber(user.getPhoneNumber());
        }

        if(status != null && status.length() > 0){
            user.setStatus(status);
        }

        if(otp != null && otp.length() > 0){
            user.setOtp(Integer.valueOf(otp));
        }
    }

    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public void ActivateUser(UserService userService,User user) {

        if(userService != null && user != null) {

            boolean isValidOtp = userService.verifyOtp(user,123456);

            if (!isValidOtp) {
                throw new IllegalStateException("Invalid otp please try again");
            } else {
                userService.updateUser(user.getId(), null, null, null, null, "Activated", null);
            }
        }
        else{
            throw new IllegalStateException("Error: Cannot activate user");
        }
    }

    public void DeactivateUser(UserService userService,User user) {
        if(userService != null && user != null) {
            userService.updateUser(user.getId(), null, null, null, null, "Deactivated",null);
        }
        else{
            throw new IllegalStateException("Error: Cannot deactivate user");
        }
    }

    public Integer sendVerificationEmailToTheUser(User user) {
        Random rdm = new Random();
        //int otp = rdm.nextInt(999999);
        int otp = 123456;
        return otp;
    }

    public boolean verifyOtp(User user,int otp){
        if(user.getOtp() == otp){
            return true;
        }
        else{
            return false;
        }
    }
}
