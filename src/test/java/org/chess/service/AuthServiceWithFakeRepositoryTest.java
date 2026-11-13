package org.chess.service;

import org.chess.controller.dtoRequests.JoinRequest;
import org.chess.controller.dtoRequests.LoginRequest;
import org.chess.controller.dtoResponse.PlayerResponse;
import org.chess.exception.InvalidLoginException;
import org.chess.exception.UsernameTakenException;
import org.chess.fake.FakePlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceWithFakeRepositoryTest {

    private static final String PASSWORD = "secret123";
    private static final LocalDate BIRTH_DATE = LocalDate.of(2004, 5, 17);

    private FakePlayerRepository fakeRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        fakeRepository = new FakePlayerRepository();
        authService = new AuthService(fakeRepository);
    }

    @Test
    void join_thenLoginWithSamePassword_returnsUsername() {
        // Arrange
        authService.join(new JoinRequest("Naruto", PASSWORD, BIRTH_DATE));

        // Act
        String username = authService.login(new LoginRequest("Naruto", PASSWORD));

        // Assert
        assertEquals("Naruto", username);
    }

    @Test
    void join_thenLoginWithWrongPassword_throwsInvalidLoginException() {
        // Arrange
        authService.join(new JoinRequest("Naruto", PASSWORD, BIRTH_DATE));
        LoginRequest request = new LoginRequest("Naruto", "wrongPassword");

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
    }

    @Test
    void join_withUsernameThatAlreadyExists_throwsUsernameTakenException() {
        // Arrange
        authService.join(new JoinRequest("Naruto", PASSWORD, BIRTH_DATE));
        JoinRequest secondRequest = new JoinRequest("Naruto", "otherPassword1", BIRTH_DATE);

        // Act and Assert
        assertThrows(UsernameTakenException.class, () -> authService.join(secondRequest));
    }

    @Test
    void join_newPlayer_startsWithZeroGames() {
        // Arrange
        PlayerService playerService = new PlayerService(fakeRepository);

        // Act
        authService.join(new JoinRequest("Naruto", PASSWORD, BIRTH_DATE));
        PlayerResponse player = playerService.getPlayerByUsername("Naruto");

        // Assert
        assertEquals(0, player.gamesPlayed());
        assertEquals(LocalDate.now(), player.creationDate());
    }

    @Test
    void login_withSeedPlayerWithoutPassword_throwsInvalidLoginException() {
        // Arrange
        fakeRepository.addPlayer("Sasuke", 10, 2, 0);
        LoginRequest request = new LoginRequest("Sasuke", PASSWORD);

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
    }
}