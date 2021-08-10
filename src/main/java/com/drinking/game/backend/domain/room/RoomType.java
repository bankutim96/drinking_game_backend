package com.drinking.game.backend.domain.room;

import lombok.Getter;

@Getter
public enum RoomType {
    PUB("PUB"),
    OUTSIDE("OUTSIDE");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }
}
