package org.chess.service;

import org.chess.controller.dtoResponse.PlayerPageResponse;
import org.chess.controller.dtoResponse.PlayerResponse;
import org.chess.exception.PlayerNotFoundException;
import org.chess.interfaces.PlayerRepository;
import org.chess.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    private static final LocalDate CREATION_DATE = LocalDate.of(2026, 3, 2);
    private static final Player NARUTO = new Player(1, "Naruto", CREATION_DATE, 8, 5, 1);

    @Mock
    private PlayerRepository playerRepository;

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService(playerRepository);
    }

    // getPlayerById

    @Test
    void getPlayerById_withExistingId_returnsPlayerWithStatistics() {
        // Arrange
        when(playerRepository.findById(1)).thenReturn(Optional.of(NARUTO));

        // Act
        PlayerResponse response = playerService.getPlayerById(1);

        // Assert
        assertEquals("Naruto", response.username());
        assertEquals(14, response.gamesPlayed());
        assertEquals(57, response.winPercentage());
    }

    @Test
    void getPlayerById_withUnknownId_throwsPlayerNotFoundException() {
        // Arrange
        when(playerRepository.findById(99)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById(99));
    }

    // getPlayerByUsername

    @Test
    void getPlayerByUsername_withExistingUsername_returnsPlayer() {
        // Arrange
        when(playerRepository.findByUsername("Naruto")).thenReturn(Optional.of(NARUTO));

        // Act
        PlayerResponse response = playerService.getPlayerByUsername("Naruto");

        // Assert
        assertEquals(1, response.id());
        assertEquals("Naruto", response.username());
    }

    @Test
    void getPlayerByUsername_withUnknownUsername_throwsPlayerNotFoundException() {
        // Arrange
        when(playerRepository.findByUsername("Nobody")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerByUsername("Nobody"));
    }

    // getPlayerPage

    @Test
    void getPlayerPage_withPageAfterLastPage_returnsLastPage() {
        // Arrange: 45 players = 3 pages of 20 (page 0, 1, 2)
        when(playerRepository.countByUsernameContaining("")).thenReturn(45);
        when(playerRepository.findPage("", 20, 40)).thenReturn(List.of(NARUTO));

        // Act
        PlayerPageResponse response = playerService.getPlayerPage(null, 10);

        // Assert
        assertEquals(2, response.page());
        assertEquals(3, response.totalPages());
        assertEquals(1, response.players().size());
    }

    @Test
    void getPlayerPage_withNegativePage_returnsFirstPage() {
        // Arrange
        when(playerRepository.countByUsernameContaining("")).thenReturn(45);
        when(playerRepository.findPage("", 20, 0)).thenReturn(List.of(NARUTO));

        // Act
        PlayerPageResponse response = playerService.getPlayerPage("", -3);

        // Assert
        assertEquals(0, response.page());
    }

    @Test
    void getPlayerPage_withMoreThanFivePagesOfPlayers_limitsToFivePages() {
        // Arrange
        when(playerRepository.countByUsernameContaining("")).thenReturn(500);

        // Act
        PlayerPageResponse response = playerService.getPlayerPage("", 0);

        // Assert
        assertEquals(5, response.totalPages());
    }

    @Test
    void getPlayerPage_withSpacesAroundSearch_searchesTrimmedText() {
        // Arrange
        when(playerRepository.countByUsernameContaining("nar")).thenReturn(1);
        when(playerRepository.findPage("nar", 20, 0)).thenReturn(List.of(NARUTO));

        // Act
        PlayerPageResponse response = playerService.getPlayerPage("  nar  ", 0);

        // Assert
        assertEquals("Naruto", response.players().get(0).username());
    }
}