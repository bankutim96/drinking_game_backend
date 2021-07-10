package com.drinking.game.backend.errorhandling.controller;

import com.drinking.game.backend.errorhandling.domain.GeneralErrorDTO;
import com.drinking.game.backend.errorhandling.exception.ConflictException;
import com.drinking.game.backend.errorhandling.exception.InvalidCredentialsException;
import com.drinking.game.backend.errorhandling.exception.InvalidTokenException;
import com.drinking.game.backend.errorhandling.util.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ExceptionMapper.mapValidationException(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<GeneralErrorDTO> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(ExceptionMapper.mapConflictException(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GeneralErrorDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(ExceptionMapper.mapInvalidCredentialsException(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<GeneralErrorDTO> handleInvalidTokenException(InvalidTokenException ex) {
        return new ResponseEntity<>(ExceptionMapper.mapInvalidTokenException(ex), HttpStatus.FORBIDDEN);
    }
}
