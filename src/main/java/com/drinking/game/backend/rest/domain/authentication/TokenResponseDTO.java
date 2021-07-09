package com.drinking.game.backend.rest.domain.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private final String accessToken;
    private final String refreshToken;
}
