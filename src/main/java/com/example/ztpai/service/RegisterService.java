package com.example.ztpai.service;

import com.example.ztpai.dto.RegistrationRequest;
import com.example.ztpai.email.MailContent;
import com.example.ztpai.email.MailService;
import com.example.ztpai.model.User;
import com.example.ztpai.model.VerificationToken;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.repository.VerificationTokenRepository;
import com.example.ztpai.security.PasswordConfig;
import com.example.ztpai.token.Token;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailContent mailContent;
    private final MailService mailService;

    @Transactional
    public ResponseEntity register(RegistrationRequest request){
        User user = new User();
        user.setPassword(passwordConfig.passwordEncoder().encode(request.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        user.setEnabled(true);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(new Token().generate());
        verificationTokenRepository.save(verificationToken);
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        mailContent.setMessage("http://localhost:8080/api/v1/auth/register/"+verificationToken.getToken());
        mailContent.setSubject("Account activation");
        mailContent.setRecipient(user.getEmail());
        mailService.sendMail(mailContent);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
