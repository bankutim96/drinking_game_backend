package com.drinking.game.backend.domain.room;

import lombok.Getter;

@Getter
public enum RoomState {
    WAITING("WAITING"),
    ONGOING("ONGOING"),
    CANCELED("CANCELED"),
    FINISHED("FINISHED");

    private final String value;

    RoomState(String value) {
        this.value = value;
    }
}
