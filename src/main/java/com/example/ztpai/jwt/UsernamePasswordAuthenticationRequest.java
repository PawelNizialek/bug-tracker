package com.example.ztpai.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsernamePasswordAuthenticationRequest {

    private String username;
    private String password;
}
