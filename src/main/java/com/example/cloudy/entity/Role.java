package com.example.cloudy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private Boolean canUpload;
    private Boolean canDownload;
    private Boolean canDelete;
    private Integer maxUploadKb;
    private LocalDateTime createdAt;
}