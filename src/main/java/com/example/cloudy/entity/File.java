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
}
