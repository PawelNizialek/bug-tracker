package com.example.ztpai.controller;

import com.example.ztpai.dto.AuthenticationResponse;
import com.example.ztpai.dto.LoginRequest;
import com.example.ztpai.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth/signin")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return loginService.login(loginRequest);
    }
}
