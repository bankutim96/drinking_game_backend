package com.drinking.game.backend.service.impl;

import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.UserCreateDTO;
import com.drinking.game.backend.rest.domain.UserDetailsDTO;
import com.drinking.game.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsDTO createUser(UserCreateDTO userCreate) {
        var user = userCreate.toUser();
        user.setPassword(passwordEncoder.encode(userCreate.getPassword()));
        userRepository.save(user);
        return UserDetailsDTO.fromUser(user);
    }
}
