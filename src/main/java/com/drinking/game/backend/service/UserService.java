package com.drinking.game.backend.service;

import com.drinking.game.backend.rest.domain.user.UserCreateDTO;
import com.drinking.game.backend.rest.domain.user.UserDetailsDTO;

public interface UserService {
    UserDetailsDTO createUser(UserCreateDTO userCreate);
}
