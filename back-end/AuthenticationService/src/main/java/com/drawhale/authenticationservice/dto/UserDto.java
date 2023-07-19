package com.drawhale.authenticationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String name;
    private String password;
    private String encryptedPassword;
    private String userId;
    private LocalDateTime createAt;
}
