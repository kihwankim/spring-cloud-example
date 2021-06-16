package com.example.userservice.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String userId;
    private LocalDateTime createAt;
    private String encryptedPwd;
}
