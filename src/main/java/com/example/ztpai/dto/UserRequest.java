package com.example.ztpai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private Long userId;
    private String FirstName;
    private String LastName;
    private String email;
    private String password;
    private String role;
    private Boolean enabled;
}
