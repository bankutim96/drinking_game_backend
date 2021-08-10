package com.drinking.game.backend.service;

import com.drinking.game.backend.domain.room.Room;
import com.drinking.game.backend.domain.room.RoomState;
import com.drinking.game.backend.domain.room.RoomType;
import com.drinking.game.backend.domain.user.User;
import com.drinking.game.backend.errorhandling.exception.DrinkingGameException;
import com.drinking.game.backend.repository.RoomRepository;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.room.RoomCreateRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomCreateResponseDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinRequestDTO;
import com.drinking.game.backend.rest.domain.room.RoomJoinResponseDTO;
import com.drinking.game.backend.security.service.AuthenticationFacade;
import com.drinking.game.backend.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
class RoomServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private RoomServiceImpl roomService;

    private final ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
    private final ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @Test
    void testCreateRoomSuccess() {
        var user = User.builder()
                .username("Test")
                .build();
        var request = RoomCreateRequestDTO.builder()
                .name("Test Room")
                .type(RoomType.PUB)
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(roomRepository.save(roomArgumentCaptor.capture())).thenAnswer(r -> r.getArguments()[0]);
        when(userRepository.save(userArgumentCaptor.capture())).thenAnswer(u -> u.getArguments()[0]);

        var response = roomService.createRoom(request);

        var actualRoom = roomArgumentCaptor.getValue();
        var actualUser = userArgumentCaptor.getValue();

        assertEquals(RoomCreateResponseDTO.fromRoom(actualRoom), response);
        assertAll(
                () -> assertEquals(request.getName(), actualRoom.getName()),
                () -> assertEquals(request.getType(), actualRoom.getType()),
                () -> assertEquals(RoomState.WAITING, actualRoom.getState()),
                () -> assertNotNull(actualRoom.getJoinCode()),
                () -> assertNotNull(actualRoom.getCreatedAt()),
                () -> assertEquals(user, actualRoom.getAdminUser())
        );
        assertNotNull(actualUser.getCurrentRoom());
    }

    @Test
    void testCreateRoomSuccessWithoutName() {
        var user = User.builder()
                .username("Test")
                .build();
        var request = RoomCreateRequestDTO.builder()
                .type(RoomType.PUB)
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(roomRepository.save(roomArgumentCaptor.capture())).thenAnswer(r -> r.getArguments()[0]);
        when(userRepository.save(userArgumentCaptor.capture())).thenAnswer(u -> u.getArguments()[0]);

        var response = roomService.createRoom(request);

        var actualRoom = roomArgumentCaptor.getValue();
        var actualUser = userArgumentCaptor.getValue();

        assertEquals(RoomCreateResponseDTO.fromRoom(actualRoom), response);
        assertAll(
                () -> assertEquals("Test's room", actualRoom.getName()),
                () -> assertEquals(request.getType(), actualRoom.getType()),
                () -> assertEquals(RoomState.WAITING, actualRoom.getState()),
                () -> assertNotNull(actualRoom.getJoinCode()),
                () -> assertNotNull(actualRoom.getCreatedAt()),
                () -> assertEquals(user, actualRoom.getAdminUser())
        );
        assertNotNull(actualUser.getCurrentRoom());

        verify(authenticationFacade).getCurrentUser();
        verify(roomRepository).save(any());
        verify(userRepository).save(any());

        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(roomRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testCreateRoomAlreadyJoined() {
        var user = User.builder()
                .username("Test")
                .currentRoom(new Room())
                .build();
        var request = RoomCreateRequestDTO.builder()
                .type(RoomType.PUB)
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);

        assertThrows(DrinkingGameException.class, () -> roomService.createRoom(request));
        verify(authenticationFacade).getCurrentUser();
        verifyNoMoreInteractions(authenticationFacade);

        verifyNoInteractions(roomRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testJoinRoomSuccess() {
        var user = new User();
        var room = new Room();
        room.setName("Test Room");
        room.setId("1");

        var request = RoomJoinRequestDTO.builder()
                .joinCode("asd123ef")
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(roomRepository.findByJoinCode(request.getJoinCode())).thenReturn(Optional.of(room));

        var response = roomService.joinRoom(request);

        assertEquals(RoomJoinResponseDTO.fromRoom(room), response);
        assertNotNull(user.getCurrentRoom());

        verify(authenticationFacade).getCurrentUser();
        verify(roomRepository).findByJoinCode(request.getJoinCode());
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testJoinRoomRoomNotFound() {
        var user = new User();

        var request = RoomJoinRequestDTO.builder()
                .joinCode("asd123ef")
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(roomRepository.findByJoinCode(request.getJoinCode())).thenReturn(Optional.empty());

        assertThrows(DrinkingGameException.class, () -> roomService.joinRoom(request));

        verify(authenticationFacade).getCurrentUser();
        verify(roomRepository).findByJoinCode(request.getJoinCode());
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testJoinRoomSuccessAlreadyJoined() {
        var user = new User();
        user.setCurrentRoom(new Room());

        var request = RoomJoinRequestDTO.builder()
                .joinCode("asd123ef")
                .build();
        when(authenticationFacade.getCurrentUser()).thenReturn(user);

        assertThrows(DrinkingGameException.class, () -> roomService.joinRoom(request));

        verify(authenticationFacade).getCurrentUser();
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoInteractions(roomRepository);
    }

    @Test
    void testQuitRoom() {
        var user = User.builder()
                .build();
        var room = Room.builder()
                .joinedUsers(Arrays.asList(user, new User()))
                .build();
        user.setCurrentRoom(room);

        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(userRepository.save(any())).thenAnswer(u -> u.getArguments()[0]);

        roomService.quitCurrentRoom();

        assertNull(user.getCurrentRoom());

        verify(authenticationFacade).getCurrentUser();
        verify(userRepository).save(any());
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testQuitRoomLastUser() {
        var user = User.builder()
                .build();
        var room = Room.builder()
                .state(RoomState.ONGOING)
                .joinedUsers(Collections.singletonList(user))
                .build();
        user.setCurrentRoom(room);

        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(userRepository.save(any())).thenAnswer(u -> u.getArguments()[0]);

        roomService.quitCurrentRoom();

        assertNull(user.getCurrentRoom());
        assertAll(
                () -> assertEquals(RoomState.FINISHED, room.getState()),
                () -> assertNotNull(room.getClosedAt())
        );

        verify(authenticationFacade).getCurrentUser();
        verify(userRepository).save(any());
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testQuitRoomLastUserNotStarted() {
        var user = User.builder()
                .build();
        var room = Room.builder()
                .state(RoomState.WAITING)
                .joinedUsers(Collections.singletonList(user))
                .build();
        user.setCurrentRoom(room);

        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(userRepository.save(any())).thenAnswer(u -> u.getArguments()[0]);

        roomService.quitCurrentRoom();

        assertNull(user.getCurrentRoom());
        assertAll(
                () -> assertEquals(RoomState.CANCELED, room.getState()),
                () -> assertNotNull(room.getClosedAt())
        );

        verify(authenticationFacade).getCurrentUser();
        verify(userRepository).save(any());
        verifyNoMoreInteractions(authenticationFacade);
        verifyNoMoreInteractions(userRepository);
    }
}
