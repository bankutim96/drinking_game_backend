package com.drinking.game.backend.service.impl;

import com.drinking.game.backend.errorhandling.ErrorCodes;
import com.drinking.game.backend.errorhandling.exception.InvalidCredentialsException;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.authentication.LoginDTO;
import com.drinking.game.backend.rest.domain.authentication.TokenResponseDTO;
import com.drinking.game.backend.security.service.TokenService;
import com.drinking.game.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponseDTO login(LoginDTO login) {
        var user = userRepository.findByUsername(login.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCodes.INVALID_LOGIN));
        if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            String accessToken = tokenService.generateAccessToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);
            return new TokenResponseDTO(accessToken, refreshToken);
        } else {
            throw new InvalidCredentialsException(ErrorCodes.INVALID_LOGIN);
        }
    }
}
