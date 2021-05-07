package com.example.ztpai.controller;

import com.example.ztpai.dto.LoginRequest;
import com.example.ztpai.dto.RegistrationRequest;
import com.example.ztpai.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @GetMapping("/register/{token}")
    public HttpStatus verifyAccount(@PathVariable("token") String token){
        authService.verifyAccount(token);
        return HttpStatus.OK;
    }
}
