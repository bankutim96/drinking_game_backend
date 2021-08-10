package com.drinking.game.backend.service.impl;

import com.drinking.game.backend.domain.room.Room;
import com.drinking.game.backend.domain.room.RoomState;
import com.drinking.game.backend.errorhandling.ErrorCodes;
import com.drinking.game.backend.errorhandling.exception.DrinkingGameException;
import com.drinking.game.backend.repository.RoomRepository;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.room.RoomCreateRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomCreateResponseDTO;
import com.drinking.game.backend.security.service.AuthenticationFacade;
import com.drinking.game.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final AuthenticationFacade authenticationFacade;

    private final RandomString codeGenerator = new RandomString(8);

    private static final String ROOM_NAME_TEMPLATE = "%s's room";

    @Override
    @Transactional
    public RoomCreateResponseDTO createRoom(RoomCreateRequestDTO request) {
        var user = authenticationFacade.getCurrentUser();
        if (user.getCurrentRoom() != null) {
            throw new DrinkingGameException(ErrorCodes.ALREADY_IN_ROOM);
        }
        var room = Room.builder()
                .name(request.getName() != null ? request.getName() : generateName(user.getUsername()))
                .type(request.getType())
                .state(RoomState.WAITING)
                .joinCode(codeGenerator.nextString())
                .createdAt(LocalDateTime.now())
                .joinedUsers(Collections.singletonList(user))
                .adminUser(user)
                .build();
        roomRepository.save(room);

        user.setCurrentRoom(room);
        userRepository.save(user);

        return RoomCreateResponseDTO.fromRoom(room);
    }

    private String generateName(String username) {
        return String.format(ROOM_NAME_TEMPLATE, username);
    }
}
