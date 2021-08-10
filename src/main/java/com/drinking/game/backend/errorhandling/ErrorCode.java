package com.drinking.game.backend.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErrorCode implements Serializable {

    private final int code;
    private final String message;
    private final HttpStatus status;

    public ErrorCode(int code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
