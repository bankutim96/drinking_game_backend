package com.drinking.game.backend.rest.domain.authentication;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LoginDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
