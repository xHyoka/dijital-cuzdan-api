package com.tunahan.starter.exception;

public class AccountAccessDeniedException extends RuntimeException {
    public AccountAccessDeniedException(String message) {
        super(message);
    }
}
