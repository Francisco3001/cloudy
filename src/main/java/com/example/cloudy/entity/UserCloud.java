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
}