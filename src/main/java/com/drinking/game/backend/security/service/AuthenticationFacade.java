package com.drinking.game.backend.security.service;

import com.drinking.game.backend.domain.user.User;

public interface AuthenticationFacade {
    User getCurrentUser();
}
