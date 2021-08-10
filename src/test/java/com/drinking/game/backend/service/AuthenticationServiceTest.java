package com.drinking.game.backend.service;

import com.drinking.game.backend.domain.user.User;
import com.drinking.game.backend.errorhandling.exception.InvalidCredentialsException;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.authentication.LoginDTO;
import com.drinking.game.backend.rest.domain.authentication.TokenResponseDTO;
import com.drinking.game.backend.security.service.TokenService;
import com.drinking.game.backend.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class AuthenticationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testLoginSuccess() {
        LoginDTO login = LoginDTO.builder()
                .username("test_user")
                .password("test_password")
                .build();

        User user = User.builder()
                .password("test_password")
                .build();
        TokenResponseDTO tokenResponse = new TokenResponseDTO("accessToken", "refreshToken");

        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(login.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenService.generateAccessToken(user)).thenReturn(tokenResponse.getAccessToken());
        when(tokenService.generateRefreshToken(user)).thenReturn(tokenResponse.getRefreshToken());

        var actualResult = authenticationService.login(login);

        assertAll(
                () -> assertEquals(tokenResponse.getAccessToken(), actualResult.getAccessToken()),
                () -> assertEquals(tokenResponse.getRefreshToken(), actualResult.getRefreshToken())
        );

        verify(userRepository).findByUsername(login.getUsername());
        verify(passwordEncoder).matches(login.getPassword(), user.getPassword());
        verify(tokenService).generateAccessToken(user);
        verify(tokenService).generateRefreshToken(user);

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(tokenService);
    }

    @Test
    void testLoginUserNotFound() {
        LoginDTO login = LoginDTO.builder()
                .username("test_user")
                .password("test_password")
                .build();
        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(login));

        verify(userRepository).findByUsername(login.getUsername());

        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
    }

    @Test
    void testLoginInvalidPassword() {
        LoginDTO login = LoginDTO.builder()
                .username("test_user")
                .password("test_password")
                .build();
        User user = User.builder()
                .password("invalid_password")
                .build();
        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(login.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(login));

        verify(userRepository).findByUsername(login.getUsername());
        verify(passwordEncoder).matches(login.getPassword(), user.getPassword());

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
    }

}
