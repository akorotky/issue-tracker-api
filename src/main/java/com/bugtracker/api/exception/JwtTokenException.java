package com.bugtracker.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenException extends RuntimeException {

    public JwtTokenException() {
        super();
    }

    public JwtTokenException(String message) {
        super(message);
    }

    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenException(Throwable cause) {
        super(cause);
    }
}