package com.example.ztpai.service;

import com.example.ztpai.dto.AuthenticationResponse;
import com.example.ztpai.dto.LoginRequest;
import com.example.ztpai.dto.LoginResponse;
import com.example.ztpai.dto.RegistrationRequest;
import com.example.ztpai.email.MailContent;
import com.example.ztpai.email.MailService;
import com.example.ztpai.exception.ZtpaiAppException;
import com.example.ztpai.jwt.Jwt;
import com.example.ztpai.model.Role;
import com.example.ztpai.model.User;
import com.example.ztpai.model.VerificationToken;
import com.example.ztpai.repository.RoleRepository;
import com.example.ztpai.repository.UserRepository;
import com.example.ztpai.repository.VerificationTokenRepository;
import com.example.ztpai.security.PasswordConfig;
import com.example.ztpai.security.UserRole;
import com.example.ztpai.token.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;
    private final Jwt jwt;
    private final MailService mailService;

    public AuthService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository,
                       AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService,
                       RoleRepository roleRepository, PasswordConfig passwordConfig, Jwt jwt, MailService mailService) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.jwt = jwt;
        this.mailService = mailService;
    }

    @Transactional
    public LoginResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwt.generateToken(authentication);
        UserDetails myUserDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        List<String> roles = myUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new LoginResponse(myUserDetails.getUsername(), jwtToken, roles);
    }

    public AuthenticationResponse registerUser(RegistrationRequest registrationRequest) {
        if(roleRepository.findAll().isEmpty()){
            List<Role> roles = new ArrayList<>();
            for (UserRole role : UserRole.values()) {
                roles.add(new Role(roles.indexOf(role), role));
            }
            roleRepository.saveAll(roles);
        }
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return new AuthenticationResponse();// TO DO
        }

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(new Token().generate());

        User user = User.builder()
                .created_at(LocalDateTime.now())
                .email(registrationRequest.getEmail())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .enabled(false)
                .password(passwordConfig.passwordEncoder().encode(registrationRequest.getPassword()))
                .verificationToken(verificationToken)
                .build();

        Set<String> userRole = registrationRequest.getRoles();
        Set<Role> roles = new HashSet<>();


        if (userRole == null) {
            Role role;
            if(user.getFirstName().equals("admin") && user.getLastName().equals("admin")){
                if(userRepository.existsByFirstNameAndLastName("admin", "admin")) return null;
                user.setEnabled(true);
                role = roleRepository.findByRoleName(UserRole.ADMIN)
                        .orElseThrow(() -> new RuntimeException("user role not found"));
            }else{
                role = roleRepository.findByRoleName(UserRole.USER)
                        .orElseThrow(() -> new RuntimeException("user role not found"));
            }
            roles.add(role);
        } else {
            userRole.forEach(role -> {
                if ("ADMIN".equals(role)) {
                    Role adminRole = roleRepository.findByRoleName(UserRole.ADMIN)
                            .orElseThrow(() -> new RuntimeException("admin role not found."));
                    roles.add(adminRole);
                } else {
                    Role roleUser = roleRepository.findByRoleName(UserRole.USER)
                            .orElseThrow(() -> new RuntimeException("user role not found."));
                    roles.add(roleUser);
                }
            });
        }
        user.setRoles(roles);
        verificationTokenRepository.save(verificationToken);
        userRepository.save(user);
        MailContent mailContent = new MailContent();
        mailContent.setMessage("http://localhost:8080/api/v1/auth/register/"+verificationToken.getToken());
        mailContent.setSubject("Account activation");
        mailContent.setRecipient(user.getEmail());
        mailService.sendMail(mailContent);
        return new AuthenticationResponse();
    }
    public void verifyAccount(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new ZtpaiAppException("invalid verification token")
        );
        User user = userRepository.findByVerificationToken(verificationToken).orElseThrow(
                () -> new ZtpaiAppException("invalid email")
        );
        user.setEnabled(true);
        userRepository.save(user);
        verificationToken.setToken(null);
        verificationTokenRepository.save(verificationToken);
    }
}
