package com.drinking.game.backend.rest.domain.room;

import com.drinking.game.backend.domain.room.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomJoinResponseDTO {
    private String id;
    private String name;

    public static RoomJoinResponseDTO fromRoom(Room room) {
        return RoomJoinResponseDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .build();
    }
}
