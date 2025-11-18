package com.example.cloudy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements ApplicationRunner {
    @Autowired
    private BCryptPasswordEncoder codificador;


    @Override
    public void run(ApplicationArguments args) throws Exception {
/*
        String pass= "admin";
        String passCifrado= codificador.encode(pass);
        Usuario usuario = new Usuario("admin1","admin1",passCifrado,"admin1", UsuarioRole.USUARIO);
        System.out.println("pass sin cifrar: "+pass+ " pass cifrado: "+passCifrado);
        usuarioRepository.save(usuario);
*/

    }
}
