package com.drinking.game.backend.errorhandling.exception;

import com.drinking.game.backend.errorhandling.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DrinkingGameException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Throwable throwable;

    public DrinkingGameException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.throwable = null;
    }
}
