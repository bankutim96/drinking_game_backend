package com.drinking.game.backend.rest.domain.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class LoginDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
