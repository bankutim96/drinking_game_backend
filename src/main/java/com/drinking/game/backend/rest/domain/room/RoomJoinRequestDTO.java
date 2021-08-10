package com.drinking.game.backend.rest.domain.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomJoinRequestDTO {
    private String joinCode;
}
