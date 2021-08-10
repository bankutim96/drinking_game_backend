package com.drinking.game.backend.rest.controller;

import com.drinking.game.backend.rest.domain.room.RoomCreateRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomCreateResponseDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinResponseDTO;
import com.drinking.game.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomCreateResponseDTO> createRoom(@RequestBody @Valid RoomCreateRequestDTO request) {
        var response = roomService.createRoom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/join")
    public ResponseEntity<RoomJoinResponseDTO> joinRoom(@RequestBody RoomJoinRequestDTO request) {
        var response = roomService.joinRoom(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/quit")
    public ResponseEntity<Void> quitRoom() {
        roomService.quitCurrentRoom();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
