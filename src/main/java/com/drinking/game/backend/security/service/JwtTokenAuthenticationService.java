package com.drinking.game.backend.security.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface JwtTokenAuthenticationService {

    UsernamePasswordAuthenticationToken getAuthenticationFromToken(String token);
}
