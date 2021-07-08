package com.drinking.game.backend.service;

import com.drinking.game.backend.rest.domain.UserCreateDTO;
import com.drinking.game.backend.rest.domain.UserDetailsDTO;

public interface UserService {
    UserDetailsDTO createUser(UserCreateDTO userCreate);
}
