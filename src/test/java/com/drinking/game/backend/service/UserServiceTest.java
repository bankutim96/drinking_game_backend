package com.drinking.game.backend.service;

import com.drinking.game.backend.domain.user.User;
import com.drinking.game.backend.errorhandling.exception.ConflictException;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.user.UserCreateDTO;
import com.drinking.game.backend.rest.domain.user.UserDetailsDTO;
import com.drinking.game.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    @Test
    void testCreateUserSuccess() {
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username("test_user")
                .email("test_email@drink.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .password("test_password")
                .build();
        User user = userCreateDTO.toUser();
        user.setPassword("encoded_password");

        UserDetailsDTO expectedResult = UserDetailsDTO.fromUser(user);

        when(passwordEncoder.encode(userCreateDTO.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(userCaptor.capture())).thenReturn(user);

        var actualResult = userService.createUser(userCreateDTO);

        assertEquals(expectedResult, actualResult);
        assertEquals(user, userCaptor.getValue());
    }

    @Test
    void testCreateUserNotUnique() {
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .username("existing_username")
                .email("existing_email@drink.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .password("test_password")
                .build();
        when(passwordEncoder.encode(any())).thenReturn(null);
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ConflictException.class, () -> userService.createUser(userCreateDTO));
    }
}
