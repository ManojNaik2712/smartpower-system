package com.smartpower.AuthException;

public class AdminSecretMismatchException extends RuntimeException{
    public AdminSecretMismatchException(String message) {
        super(message);
    }
}
