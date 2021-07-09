package com.drinking.game.backend.rest.controller;

import com.drinking.game.backend.rest.domain.authentication.LoginDTO;
import com.drinking.game.backend.rest.domain.authentication.TokenResponseDTO;
import com.drinking.game.backend.rest.domain.user.UserCreateDTO;
import com.drinking.game.backend.rest.domain.user.UserDetailsDTO;
import com.drinking.game.backend.service.AuthenticationService;
import com.drinking.game.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> createUser(@RequestBody @Valid UserCreateDTO userCreate) {
        UserDetailsDTO user = userService.createUser(userCreate);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO login) {
        TokenResponseDTO tokenResponse = authenticationService.login(login);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
