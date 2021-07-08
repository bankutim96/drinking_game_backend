package com.drinking.game.backend.rest.domain;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
public class UserDetailsDTO {
    private final String id;
    private final String username;
    private final String email;
    private final Role role;
    private final LocalDate dateOfBirth;

    public static UserDetailsDTO fromUser(User user) {
        return UserDetailsDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}
