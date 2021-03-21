package com.example.ztpai.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String firstname;
    private String lastname;
    private String password;
}
