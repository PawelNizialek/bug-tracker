package com.example.ztpai.token;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VerificationTokenGenerator {
    public String generate(){
        return UUID.randomUUID().toString();
    }
}
