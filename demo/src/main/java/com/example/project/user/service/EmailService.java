package com.example.project.user.service;

import com.example.project.user.model.User;
import com.example.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class EmailService {

    private final UserRepository userRepository;

    @Autowired
    public EmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public void sendVerificationEmailToTheUser(Integer userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("Failed to send verification email"));

        Random random = new Random();
        int otp = random.nextInt(999999);
        user.setOtp(otp);

    }

    public boolean verifyOtp(int generatedOtp,int receivedOtp){
        return(generatedOtp == receivedOtp);
    }
}
