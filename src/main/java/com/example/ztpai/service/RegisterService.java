package com.example.ztpai.service;

import com.example.ztpai.model.User;
import com.example.ztpai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    @Autowired
    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User user){
        userRepository.save(user);
        return "registered";
    }
}
