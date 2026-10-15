package org.chess.controller.dtoResponse;

import org.chess.model.Player;

import java.time.LocalDate;

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

    // Factory method: to convert.
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