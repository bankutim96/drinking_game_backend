package com.drinking.game.backend.errorhandling;

import org.springframework.http.HttpStatus;

public class ErrorCodes {

    private ErrorCodes() {
    }

    public static final ErrorCode INVALID_LOGIN = new ErrorCode(4010, "INVALID_LOGIN");

    public static final ErrorCode INVALID_TOKEN = new ErrorCode(4030, "INVALID_TOKEN");

    //New ErrorCodes
    public static final ErrorCode NO_USER_IN_CONTEXT = new ErrorCode(10100, "No user is currently logged in", HttpStatus.UNAUTHORIZED);
    public static final ErrorCode ALREADY_IN_ROOM = new ErrorCode(10201, "User is already joined to another room", HttpStatus.METHOD_NOT_ALLOWED);
    public static final ErrorCode ROOM_NOT_FOUND = new ErrorCode(40401, "Room cannot be found", HttpStatus.NOT_FOUND);
}
