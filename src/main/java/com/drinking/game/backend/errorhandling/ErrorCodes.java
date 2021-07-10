package com.drinking.game.backend.errorhandling;

public class ErrorCodes {

    private ErrorCodes() {
    }

    public static final ErrorCode INVALID_LOGIN = new ErrorCode(4010, "INVALID_LOGIN");

    public static final ErrorCode INVALID_TOKEN = new ErrorCode(4030, "INVALID_TOKEN");
}
