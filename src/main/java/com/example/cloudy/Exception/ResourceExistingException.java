package com.example.cloudy.Exception;

public class ResourceExistingException extends RuntimeException {
    public ResourceExistingException(String message) {
        super(message);
    }
}
