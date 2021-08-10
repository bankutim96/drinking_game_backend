package com.drinking.game.backend.service;

import com.drinking.game.backend.rest.domain.room.RoomCreateRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomCreateResponseDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinResponseDTO;

public interface RoomService {
    RoomCreateResponseDTO createRoom(RoomCreateRequestDTO request);

    RoomJoinResponseDTO joinRoom(RoomJoinRequestDTO request);

    void quitCurrentRoom();
}
