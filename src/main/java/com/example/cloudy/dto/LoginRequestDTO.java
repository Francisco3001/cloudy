package com.example.cloudy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    public String identifier;  // email o username
    public String password;
}
