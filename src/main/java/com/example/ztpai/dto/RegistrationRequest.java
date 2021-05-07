package com.example.ztpai.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RegistrationRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}
