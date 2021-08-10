package com.drinking.game.backend.service;

import com.drinking.game.backend.rest.domain.authentication.LoginDTO;
import com.drinking.game.backend.rest.domain.authentication.TokenResponseDTO;

public interface AuthenticationService {
    TokenResponseDTO login(LoginDTO login);
}
