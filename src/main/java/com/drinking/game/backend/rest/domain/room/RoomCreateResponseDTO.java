package com.drinking.game.backend.rest.domain.room;

import com.drinking.game.backend.domain.room.Room;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomCreateResponseDTO {

    private String id;
    private String name;
    private String joinCode;

    public static RoomCreateResponseDTO fromRoom(Room room) {
        return new RoomCreateResponseDTO(room.getId(), room.getName(), room.getJoinCode());
    }
}
