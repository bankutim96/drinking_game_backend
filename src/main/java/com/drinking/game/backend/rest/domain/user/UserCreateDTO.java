package com.drinking.game.backend.rest.domain.user;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.domain.user.User;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
public class UserCreateDTO {
    @NotNull
    @Size(min = 6, max = 18)
    private String username;
    @NotNull
    private String email;
    @NotNull
    @Size(min = 8, max = 24)
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
