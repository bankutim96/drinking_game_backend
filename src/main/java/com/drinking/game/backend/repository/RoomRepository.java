package com.drinking.game.backend.repository;

import com.drinking.game.backend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByJoinCode(String joinCode);
}
