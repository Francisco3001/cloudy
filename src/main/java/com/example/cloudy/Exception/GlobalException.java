package com.example.cloudy.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> notFound(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ResourceExistingException.class)
    public ResponseEntity<String> conflict(ResourceExistingException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // ------------------ Validaciones / JSON ------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El formato del JSON enviado es inválido");
    }

    // ------------------ JWT ------------------
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> tokenExpirado(ExpiredJwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("El token ha expirado");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tokenInvalido(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("No se pudo procesar el token");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> tokenMalFormado(MalformedJwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("El token es inválido o está mal formado");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> firmaInvalida(SignatureException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("La firma del token no es válida");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado: " + e.getMessage());
    }

}
