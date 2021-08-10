package com.drinking.game.backend.errorhandling.exception;

import com.drinking.game.backend.errorhandling.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidCredentialsException extends RuntimeException {
    private final ErrorCode errorCode;
}
