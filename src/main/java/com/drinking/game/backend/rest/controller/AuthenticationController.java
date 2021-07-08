package com.drinking.game.backend.rest.controller;

import com.drinking.game.backend.rest.domain.UserCreateDTO;
import com.drinking.game.backend.rest.domain.UserDetailsDTO;
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

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> createUser(@RequestBody @Valid UserCreateDTO userCreate){
        UserDetailsDTO user = userService.createUser(userCreate);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
