package com.example.cloudy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private LocalDateTime createdAt;

    public User(String username, String email, String password, Boolean enabled, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }
}