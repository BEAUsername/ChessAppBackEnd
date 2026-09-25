package org.chess.dto;

import org.chess.model.Player;

import java.time.LocalDate;

// Record: Java generates the private final fields, constructor and getters
public record PlayerResponse(
        int id,
        String username,
        LocalDate creationDate,
        int wins,
        int losses,
        int draws,
        int gamesPlayed,
        int winPercentage
) {

    // Factory method: converts a Player (model) into a PlayerResponse (DTO)
    public static PlayerResponse fromPlayer(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getUsername(),
                player.getCreationDate(),
                player.getWins(),
                player.getLosses(),
                player.getDraws(),
                player.getGamesPlayed(),
                player.getWinPercentage()
        );
    }
}