package com.example.ztpai.service;

import com.example.ztpai.dto.RegistrationRequest;
import com.example.ztpai.model.User;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.security.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;

    public ResponseEntity register(@RequestBody RegistrationRequest request){
        User user = new User();
        user.setPassword(passwordConfig.passwordEncoder().encode(request.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        user.setEnabled(false);
        user.setRole("USER");
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
