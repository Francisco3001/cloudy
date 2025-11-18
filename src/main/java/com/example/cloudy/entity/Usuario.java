package com.example.cloudy.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String password;
    private  UsuarioRole role;
}
