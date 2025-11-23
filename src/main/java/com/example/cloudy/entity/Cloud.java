package com.example.cloudy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Cloud {
    private Long id;
    private String name;
    private Long maxBytes;
    private Long usedBytes;
    private String path;
    private LocalDateTime createdAt;
}