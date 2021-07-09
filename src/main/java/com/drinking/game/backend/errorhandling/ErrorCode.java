package com.drinking.game.backend.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErrorCode implements Serializable {
    private final int code;
    private final String message;
}
