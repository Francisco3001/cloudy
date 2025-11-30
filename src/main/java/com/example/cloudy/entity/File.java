package com.example.cloudy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class File {
    private Long id;
    private Long cloudId;
    private Long userId;
    private String name;
    private Long bytes;
    private String path;
    private String hashSha256;
    private LocalDateTime createdAt;

    public File(Long cloudId, Long userId, String name, Long bytes, String path, String hashSha256, LocalDateTime createdAt) {
        this.cloudId = cloudId;
        this.userId = userId;
        this.name = name;
        this.bytes = bytes;
        this.path = path;
        this.hashSha256 = hashSha256;
        this.createdAt = createdAt;
    }
}
