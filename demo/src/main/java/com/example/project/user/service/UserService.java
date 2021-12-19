package com.example.project.user.service;

import com.example.project.user.model.User;
import com.example.project.user.dto.UserDTO;
import com.example.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.project.utility.PhoneNumberValidator;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public UserService(UserRepository userRepository, PhoneNumberValidator phoneNumberValidator,EmailService emailService){
        this.userRepository = userRepository;
        this.phoneNumberValidator = phoneNumberValidator;
        this.emailService = emailService;
    }

    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User addNewUser(UserDTO userDTO) {

        Optional<User> userOptionalEmail = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> userOptionalUserName = userRepository.findByUserName(userDTO.getUserName());

        if(userOptionalUserName.isPresent()){
            throw new IllegalStateException("UserName is not available.");
        }

        if(userOptionalEmail.isPresent()){
            throw new IllegalStateException("Email is already registered by other user.");
        }

        boolean isValid = phoneNumberValidator.validate(userDTO.getPhoneNumber()); // use custom validators on model, use lo,bok instead

        if(!isValid) {
            throw new IllegalStateException("Invalid Phone Number Entered.");
        }

        User user = new User(null,userDTO.getUserName(),userDTO.getFirstAndLastName(),userDTO.getEmail(),userDTO.getPhoneNumber(),false,null);

        userRepository.save(user);

        return user;

    }

    @Transactional
    public void updateUser(Integer userId,UserDTO userDTO) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User With Id " + userId + " does not exist"));

        if(userDTO.getUserName() != null && userDTO.getUserName().length() > 0) {
            if ((user.getUserName()).equals(userDTO.getUserName())) {
                throw new IllegalStateException("UserName is not available.");
            } else {
                user.setUserName(userDTO.getUserName());
            }
        }

        if(userDTO.getFirstAndLastName() != null && (userDTO.getFirstAndLastName().length() > 0)){
            user.setFirstAndLastName(userDTO.getFirstAndLastName());
        }

        if(userDTO.getEmail() != null && userDTO.getEmail().length() > 0){
            if(user.getEmail().equals(userDTO.getEmail())){
                throw new IllegalStateException("Old Email Address Entered");
            }else {
                user.setEmail(userDTO.getEmail());
                user.setStatus(false);
                emailService.sendVerificationEmailToTheUser(user.getId());
            }
        }

        if(userDTO.getPhoneNumber() != null && userDTO.getPhoneNumber().length() > 0) {
            if (!(phoneNumberValidator.validate(userDTO.getPhoneNumber()))) {
                throw new IllegalStateException("Invalid Phone Number Entered.");
            } else {
                user.setPhoneNumber(userDTO.getPhoneNumber());
            }
        }
    }

    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void activateUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User With Id " + userId + " does not exist"));
        boolean isValidOtp = this.verifyOtp(user.getOtp(),user.getOtp());

        if (!isValidOtp) {
            throw new IllegalStateException("Invalid otp please try again");
        } else {
            user.setStatus(true);
        }
    }

    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User With Id " + userId + " does not exist"));
        user.setStatus(false);
    }

    @Transactional
    public void sendVerificationEmailToTheUser(Integer userId) {
        emailService.sendVerificationEmailToTheUser(userId);
    }

    public boolean verifyOtp(int generatedOtp,int receivedOtp){
        return(emailService.verifyOtp(generatedOtp,receivedOtp));
    }
}
