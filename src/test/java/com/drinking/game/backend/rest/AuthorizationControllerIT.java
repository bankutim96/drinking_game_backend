package com.drinking.game.backend.rest;

import com.drinking.game.backend.domain.user.Role;
import com.drinking.game.backend.repository.UserRepository;
import com.drinking.game.backend.rest.domain.authentication.LoginDTO;
import com.drinking.game.backend.rest.domain.authentication.TokenResponseDTO;
import com.drinking.game.backend.rest.domain.user.UserCreateDTO;
import com.drinking.game.backend.rest.domain.user.UserDetailsDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizationControllerIT {

    @Value("${local.server.port}")
    private int randomServerPort;

    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().
                baseUrl("http://localhost:" + randomServerPort + "/api/v1")
                .responseTimeout(Duration.ofMinutes(1))
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() {
        var userCreate = UserCreateDTO.builder()
                .username("testuser")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();

        var actualResult = client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserDetailsDTO.class)
                .returnResult().getResponseBody();

        var optUser = userRepository.findByUsername(userCreate.getUsername());

        assumeTrue(optUser.isPresent());
        var user = optUser.get();

        assertAll(
                () -> assertNotNull(user.getId()),
                () -> assertEquals(userCreate.getUsername(), user.getUsername()),
                () -> assertTrue(passwordEncoder.matches(userCreate.getPassword(), user.getPassword())),
                () -> assertEquals(userCreate.getEmail(), user.getEmail()),
                () -> assertEquals(Role.USER, user.getRole()),
                () -> assertEquals(userCreate.getDateOfBirth(), user.getDateOfBirth())
        );

        assertAll(
                () -> assertNotNull(actualResult),
                () -> assertNotNull(Objects.requireNonNull(actualResult).getId()),
                () -> assertEquals(userCreate.getUsername(), Objects.requireNonNull(actualResult).getUsername()),
                () -> assertEquals(userCreate.getEmail(), Objects.requireNonNull(actualResult).getEmail()),
                () -> assertEquals(Role.USER, Objects.requireNonNull(actualResult).getRole()),
                () -> assertEquals(userCreate.getDateOfBirth(), Objects.requireNonNull(actualResult).getDateOfBirth())
        );
    }

    @Test
    void testRegisterNotUnique() {
        var userCreate = UserCreateDTO.builder()
                .username("testuser")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();

        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isCreated();
        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void testRegisterMalformed() {
        var userCreate = UserCreateDTO.builder()
                .username("short")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();
        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    void testLoginSuccess() {
        var userCreate = UserCreateDTO.builder()
                .username("testuser")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();
        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isCreated();

        var login = LoginDTO.builder()
                .username(userCreate.getUsername())
                .password(userCreate.getPassword())
                .build();
        var result = client.post().uri("/auth/login")
                .bodyValue(login)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenResponseDTO.class)
                .returnResult().getResponseBody();

        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(Objects.requireNonNull(result).getAccessToken()),
                () -> assertNotNull(Objects.requireNonNull(result).getRefreshToken())
        );
    }

    @Test
    void testLoginInvalidUsername() {
        var userCreate = UserCreateDTO.builder()
                .username("testuser")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();
        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isCreated();

        var login = LoginDTO.builder()
                .username(userCreate.getUsername() + "invalid")
                .password(userCreate.getPassword())
                .build();
        client.post().uri("/auth/login")
                .bodyValue(login)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testLoginInvalidPassword() {
        var userCreate = UserCreateDTO.builder()
                .username("testuser")
                .password("password")
                .email("test_user@drinking.com")
                .dateOfBirth(LocalDate.of(1996, 6, 8))
                .build();
        client.post().uri("/auth/register")
                .bodyValue(userCreate)
                .exchange()
                .expectStatus().isCreated();

        var login = LoginDTO.builder()
                .username(userCreate.getUsername())
                .password(userCreate.getPassword() + "invalid")
                .build();
        client.post().uri("/auth/login")
                .bodyValue(login)
                .exchange()
                .expectStatus().isUnauthorized();
    }

}
