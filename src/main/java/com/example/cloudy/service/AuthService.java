package com.example.cloudy.service;

import com.example.cloudy.Exception.GlobalException;
import com.example.cloudy.Exception.ResourceExistingException;
import com.example.cloudy.Exception.ResourceNotFoundException;
import com.example.cloudy.dto.LoginRequestDTO;
import com.example.cloudy.dto.LoginResponseDTO;
import com.example.cloudy.dto.RegisterRequestDTO;
import com.example.cloudy.entity.User;
import com.example.cloudy.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // BCrypt
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new ResourceExistingException("Email ya registrado");

        if (userRepository.findByUsername(dto.getUsername()).isPresent())
            throw new ResourceExistingException("Username ya registrado");

        String hashed = passwordEncoder.encode(dto.getPassword());

        User user = new User(
                dto.getUsername(),
                dto.getEmail(),
                hashed,
                true,
                LocalDateTime.now()
        );

        userRepository.save(user);
    }


    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository
                .findByEmail(dto.getIdentifier())
                .or(() -> userRepository.findByUsername(dto.getIdentifier()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(token);
    }
}
