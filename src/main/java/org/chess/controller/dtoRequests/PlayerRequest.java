package org.chess.controller.dtoRequests;

import org.chess.model.Player;

import java.time.LocalDate;

public record PlayerRequest(
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
    public static PlayerRequest fromPlayer(Player player) {
        return new PlayerRequest(
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