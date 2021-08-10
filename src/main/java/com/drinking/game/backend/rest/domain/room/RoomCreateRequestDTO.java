package com.drinking.game.backend.rest.domain.room;

import com.drinking.game.backend.domain.room.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequestDTO {

    private String name;
    private RoomType type;
}
