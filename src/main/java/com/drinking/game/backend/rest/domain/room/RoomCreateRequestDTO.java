package com.drinking.game.backend.rest.domain.room;

import com.drinking.game.backend.domain.room.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomCreateRequestDTO {

    private String name;
    private RoomType type;
}
