package com.drinking.game.backend.rest.domain;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.domain.user.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserCreateDTO {
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;

    public User toUser() {
        return User.builder()
                .username(username)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .role(Role.USER)
                .build();
    }
}
