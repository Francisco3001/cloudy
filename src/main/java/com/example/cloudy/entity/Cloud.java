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

    public Cloud(String name, Long maxBytes, Long usedBytes, String path, LocalDateTime createdAt) {
        this.name = name;
        this.maxBytes = maxBytes;
        this.usedBytes = usedBytes;
        this.path = path;
        this.createdAt = createdAt;
    }
}