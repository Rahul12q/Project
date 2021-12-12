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

    private final UserRepository userRepository;

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

    @Transactional
    public void addNewUser(UserDTO userDTO) {

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

        User user = new User();

        user.setUserName(userDTO.getUserName());
        user.setFirstAndLastName(userDTO.getFirstAndLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(user);

    }

    @Transactional
    public void updateUser(Integer userId,UserDTO userDTO) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));

        if(userDTO.getUserName() != null && userDTO.getUserName().length() > 0) {
            if ((user.getUserName()).equals(userDTO.getUserName())) {
                throw new IllegalStateException("UserName is not available.");
            } else
            {
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
            }
        }

        //TODO: just make sure if email, phone is changed, use needs to revalidate the email and phone
        boolean isValid = phoneNumberValidator.validate(user.getPhoneNumber());

        if(!isValid) {
            throw new IllegalStateException("Invalid Phone Number Entered.");
        }else {
            user.setPhoneNumber(user.getPhoneNumber());
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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));

        //TODO: generate otp while adding user and save it in db, get otp from db.
        boolean isValidOtp = this.verifyOtp(user.getOtp(),user.getOtp());

        if (!isValidOtp) {
            throw new IllegalStateException("Invalid otp please try again");
        } else {
            user.setStatus(true);
        }
    }

    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User With Id " + userId + " does not exist"
        ));
        user.setStatus(false);
    }

    @Transactional
    public void sendVerificationEmailToTheUser(Integer userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "Failed to send verification email"
        ));

        Random random = new Random();
        int otp = random.nextInt(999999);
        user.setOtp(otp);
    }

    public boolean verifyOtp(int generatedOtp,int receivedOtp){
        return(generatedOtp == receivedOtp);
    }

    public User findUserByUserName(String userName) {
        User user;
        user= userRepository.findByUserName(userName).orElseThrow(() -> new IllegalStateException(
                "User " +  userName + " does not exist"
        ));
        return user;
    }
}
