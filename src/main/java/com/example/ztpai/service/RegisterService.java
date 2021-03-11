package com.example.ztpai.service;

import com.example.ztpai.model.User;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;

    @Autowired
    public RegisterService(UserRepository userRepository, PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.passwordConfig = passwordConfig;
    }

    public void register(User user){
        user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        user.setEnabled(true);
        user.setRole("USER");
        userRepository.save(user);
    }
}
