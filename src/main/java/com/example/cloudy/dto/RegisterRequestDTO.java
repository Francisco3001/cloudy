package com.example.cloudy.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    public String username;
    public String email;
    public String password;
}
