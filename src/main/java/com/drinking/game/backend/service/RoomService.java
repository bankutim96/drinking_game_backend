package com.drinking.game.backend.service;

import com.drinking.game.backend.rest.domain.room.RoomCreateRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomCreateResponseDTO;

public interface RoomService {
    RoomCreateResponseDTO createRoom(RoomCreateRequestDTO request);
}
