package com.drinking.game.backend.rest.domain.user;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRoomDTO {
    private String username;
    private String currentRoomName;
}
