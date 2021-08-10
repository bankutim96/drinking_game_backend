package com.drinking.game.backend.rest.domain.user;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.domain.user.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDetailsDTO {
    private String id;
    private String username;
    private String email;
    private Role role;
    private LocalDate dateOfBirth;

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
