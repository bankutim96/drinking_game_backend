package com.drinking.game.backend.errorhandling.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GeneralErrorDTO {

    private final int errorCode;
    private final String errorMessage;
    private String friendlyMassage;
}
