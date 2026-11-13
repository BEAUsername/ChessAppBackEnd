package org.chess.service;

import org.chess.controller.dtoRequests.JoinRequest;
import org.chess.controller.dtoRequests.LoginRequest;
import org.chess.exception.InvalidLoginException;
import org.chess.exception.UsernameTakenException;
import org.chess.interfaces.PlayerRepository;
import org.chess.model.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String USERNAME = "Naruto";
    private static final String PASSWORD = "secret123";
    private static final String PASSWORD_HASH = new BCryptPasswordEncoder().encode(PASSWORD);
    private static final LocalDate BIRTH_DATE = LocalDate.of(2004, 5, 17);

    @Mock
    private PlayerRepository playerRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(playerRepository);
    }

    // join

    @Test
    void join_withValidData_savesPlayerWithHashedPassword() {
        // Arrange
        JoinRequest request = new JoinRequest(USERNAME, PASSWORD, BIRTH_DATE);
        when(playerRepository.existsByUsername(USERNAME)).thenReturn(false);
        ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        authService.join(request);

        // Assert
        verify(playerRepository).insert(eq(USERNAME), eq(LocalDate.now()), hashCaptor.capture(), eq(BIRTH_DATE));
        String savedHash = hashCaptor.getValue();
        assertNotEquals(PASSWORD, savedHash);
        assertTrue(new BCryptPasswordEncoder().matches(PASSWORD, savedHash));
    }

    @Test
    void join_withTakenUsername_throwsUsernameTakenException() {
        // Arrange
        JoinRequest request = new JoinRequest(USERNAME, PASSWORD, BIRTH_DATE);
        when(playerRepository.existsByUsername(USERNAME)).thenReturn(true);

        // Act and Assert
        assertThrows(UsernameTakenException.class, () -> authService.join(request));
        verify(playerRepository, never()).insert(any(), any(), any(), any());
    }

    @Test
    void join_withTooShortUsername_throwsException() {
        // Arrange
        JoinRequest request = new JoinRequest("ab", PASSWORD, BIRTH_DATE);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authService.join(request));
        verifyNoInteractions(playerRepository);
    }

    @Test
    void join_withTooShortPassword_throwsException() {
        // Arrange
        JoinRequest request = new JoinRequest(USERNAME, "short1", BIRTH_DATE);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authService.join(request));
        verifyNoInteractions(playerRepository);
    }

    @Test
    void join_withBirthDateInFuture_throwsException() {
        // Arrange
        JoinRequest request = new JoinRequest(USERNAME, PASSWORD, LocalDate.now().plusDays(1));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authService.join(request));
        verifyNoInteractions(playerRepository);
    }

    @Test
    void join_withoutBirthDate_throwsException() {
        // Arrange
        JoinRequest request = new JoinRequest(USERNAME, PASSWORD, null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authService.join(request));
        verifyNoInteractions(playerRepository);
    }

    // login

    @Test
    void login_withCorrectPassword_returnsStoredUsername() {
        // Arrange
        when(playerRepository.findCredentialsByUsername("naruto"))
                .thenReturn(Optional.of(new Credentials(USERNAME, PASSWORD_HASH)));

        // Act
        String username = authService.login(new LoginRequest("naruto", PASSWORD));

        // Assert
        assertEquals(USERNAME, username);
    }

    @Test
    void login_withWrongPassword_throwsInvalidLoginException() {
        // Arrange
        when(playerRepository.findCredentialsByUsername(USERNAME))
                .thenReturn(Optional.of(new Credentials(USERNAME, PASSWORD_HASH)));
        LoginRequest request = new LoginRequest(USERNAME, "wrongPassword");

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
    }

    @Test
    void login_withUnknownUsername_throwsInvalidLoginException() {
        // Arrange
        when(playerRepository.findCredentialsByUsername("Nobody")).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest("Nobody", PASSWORD);

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
    }

    @Test
    void login_withAccountWithoutPassword_throwsInvalidLoginException() {
        // Arrange
        when(playerRepository.findCredentialsByUsername("OldPlayer"))
                .thenReturn(Optional.of(new Credentials("OldPlayer", null)));
        LoginRequest request = new LoginRequest("OldPlayer", PASSWORD);

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
    }

    @Test
    void login_withoutPassword_throwsInvalidLoginException() {
        // Arrange
        LoginRequest request = new LoginRequest(USERNAME, null);

        // Act and Assert
        assertThrows(InvalidLoginException.class, () -> authService.login(request));
        verifyNoInteractions(playerRepository);
    }
}