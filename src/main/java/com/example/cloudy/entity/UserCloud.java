package com.example.cloudy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserCloud {

    private Long id;
    private Long userId;
    private Long cloudId;
    private Long roleId;
    private LocalDateTime createdAt;

    public UserCloud(Long userId, Long cloudId, Long roleId, LocalDateTime createdAt) {
        this.userId = userId;
        this.cloudId = cloudId;
        this.roleId = roleId;
        this.createdAt = createdAt;
    }
}