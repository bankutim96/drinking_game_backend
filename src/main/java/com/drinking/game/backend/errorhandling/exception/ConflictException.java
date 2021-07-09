package com.drinking.game.backend.errorhandling.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConflictException extends RuntimeException {
    private final Throwable cause;
    private final Class domainClass;
}
