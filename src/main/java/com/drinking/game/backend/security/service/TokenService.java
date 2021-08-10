package com.drinking.game.backend.security.service;

import com.drinking.game.backend.domain.user.User;

public interface TokenService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);
}
