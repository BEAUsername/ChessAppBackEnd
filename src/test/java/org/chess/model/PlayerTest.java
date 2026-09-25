package org.chess.model;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class PlayerTest {
    private static final LocalDate CREATION_DATE = LocalDate.of(2025, 1, 15);

    @Test
    void getWinPercentage_withWinsAndLosses_returnsRoundedPercentage() {
// Arrange
        Player player = new Player(1, "queenbee", CREATION_DATE, 8, 5, 1);
// Act
        int winPercentage = player.getWinPercentage();
// Assert
        assertEquals(57, winPercentage);
    }

    @Test
    void getWinPercentage_withNoGamesPlayed_returnsZero() {
// Arrange
        Player player = new Player(2, "pawnstar", CREATION_DATE, 0, 0, 0);
// Act
        int winPercentage = player.getWinPercentage();
// Assert
        assertEquals(0, winPercentage);
    }

    @Test
    void constructor_withTooShortUsername_throwsException() {
// Arrange
        String tooShortUsername = "ab";
// Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Player(3, tooShortUsername, CREATION_DATE, 0, 0, 0));
    }

    @Test
    void constructor_withNegativeWins_throwsException() {
// Arrange
        int negativeWins = -1;
// Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Player(4, "knightrider", CREATION_DATE, negativeWins, 0, 0));
    }
}