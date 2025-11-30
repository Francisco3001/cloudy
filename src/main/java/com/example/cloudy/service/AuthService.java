package com.example.cloudy.service;

import com.example.cloudy.Exception.GlobalException;
import com.example.cloudy.Exception.ResourceExistingException;
import com.example.cloudy.Exception.ResourceNotFoundException;
import com.example.cloudy.dto.LoginRequestDTO;
import com.example.cloudy.dto.LoginResponseDTO;
import com.example.cloudy.dto.RegisterRequestDTO;
import com.example.cloudy.dto.RegisterResponseDTO;
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

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
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

        String token = jwtService.generateToken(user);
        return new RegisterResponseDTO(token);
    }


    public LoginResponseDTO login(LoginRequestDTO dto) {
        String identifier = dto.getIdentifier();
        User user;
        boolean isEmail = identifier.matches(EMAIL_REGEX);

        if (isEmail) {
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        } else {
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(token);
    }
}
