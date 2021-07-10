package com.drinking.game.backend.errorhandling.util;

import com.drinking.game.backend.errorhandling.domain.GeneralErrorDTO;
import com.drinking.game.backend.errorhandling.exception.ConflictException;
import com.drinking.game.backend.errorhandling.exception.InvalidCredentialsException;
import com.drinking.game.backend.errorhandling.exception.InvalidTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

public class ExceptionMapper {

    private static final String VALIDATION_TEMPLATE = "Field: %s must be between %d and %d!";
    private static final String NULL_TEMPLATE = "Field: %s cannot be null!";
    private static final String CONFLICT_TEMPLATE = "Unique constraints are not satisfied for %s";

    private ExceptionMapper() {
    }

    public static GeneralErrorDTO mapValidationException(MethodArgumentNotValidException ex) {
        GeneralErrorDTO error = GeneralErrorDTO.builder()
                .errorCode(4220)
                .errorMessage("VALIDATION_ERROR")
                .build();
        if (ex != null && ex.hasErrors()) {
            var firstError = ex.getBindingResult().getFieldErrors().get(0);
            var field = firstError.getField();
            if ("Size".equals(firstError.getCode())) {
                Integer max = (Integer) Objects.requireNonNull(firstError.getArguments())[1];
                Integer min = (Integer) Objects.requireNonNull(firstError.getArguments())[2];
                error.setFriendlyMassage(String.format(VALIDATION_TEMPLATE, field, min, max));
            } else {
                error.setFriendlyMassage(String.format(NULL_TEMPLATE, field));
            }
        } else {
            error.setFriendlyMassage("General validation error");
        }
        return error;
    }

    public static GeneralErrorDTO mapConflictException(ConflictException ex) {
        return GeneralErrorDTO.builder()
                .errorCode(4090)
                .errorMessage("CONFLICT_EXCEPTION")
                .friendlyMassage(String.format(CONFLICT_TEMPLATE, ex.getDomainClass().getSimpleName()))
                .build();
    }

    public static GeneralErrorDTO mapInvalidCredentialsException(InvalidCredentialsException ex) {
        return GeneralErrorDTO.builder()
                .errorCode(ex.getErrorCode().getCode())
                .errorMessage(ex.getErrorCode().getMessage())
                .friendlyMassage("Invalid username or password!")
                .build();
    }

    public static GeneralErrorDTO mapInvalidTokenException(InvalidTokenException ex) {
        return GeneralErrorDTO.builder()
                .errorCode(ex.getErrorCode().getCode())
                .errorMessage(ex.getErrorCode().getMessage())
                .friendlyMassage("Given token is invalid or expired!")
                .build();
    }
}
