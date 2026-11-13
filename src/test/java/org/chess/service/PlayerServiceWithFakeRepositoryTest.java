package org.chess.service;

import org.chess.controller.dtoResponse.PlayerPageResponse;
import org.chess.fake.FakePlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerServiceWithFakeRepositoryTest {

    private FakePlayerRepository fakeRepository;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        fakeRepository = new FakePlayerRepository();
        playerService = new PlayerService(fakeRepository);
    }

    @Test
    void getPlayerPage_withSearch_returnsMatchingPlayersWithMostWinsFirst() {
        // Arrange
        fakeRepository.addPlayer("Sakura", 3, 1, 0);
        fakeRepository.addPlayer("Naruto", 8, 5, 1);
        fakeRepository.addPlayer("Sasuke", 10, 2, 0);
        fakeRepository.addPlayer("Kakashi", 20, 0, 0);

        // Act
        PlayerPageResponse response = playerService.getPlayerPage("sa", 0);

        // Assert
        assertEquals(2, response.players().size());
        assertEquals("Sasuke", response.players().get(0).username());
        assertEquals("Sakura", response.players().get(1).username());
    }

    @Test
    void getPlayerPage_secondPage_startsAtTwentyFirstPlayer() {
        // Arrange: Player1 has 1 win ... Player45 has 45 wins
        for (int number = 1; number <= 45; number++) {
            fakeRepository.addPlayer("Player" + number, number, 0, 0);
        }

        // Act
        PlayerPageResponse response = playerService.getPlayerPage("", 1);

        // Assert
        assertEquals(3, response.totalPages());
        assertEquals(20, response.players().size());
        assertEquals("Player25", response.players().get(0).username());
    }
}