package com.drinking.game.backend.rest.domain.user;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
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
