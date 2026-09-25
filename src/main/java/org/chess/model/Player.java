package org.chess.model;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Player {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]{3,12}$");

    // Fields
    private final int id;
    private final String username;
    private final LocalDate creationDate;
    private final int wins;
    private final int losses;
    private final int draws;

    // Constructor
    public Player(int id, String username, LocalDate creationDate, int wins, int losses, int draws) {
        validateId(id);
        validateUsername(username);
        validateCreationDate(creationDate);
        validateStatistic("Wins", wins);
        validateStatistic("Losses", losses);
        validateStatistic("Draws", draws);

        this.id = id;
        this.username = username;
        this.creationDate = creationDate;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    // Business logic
    public int getGamesPlayed() {
        return wins + losses + draws;
    }

    public int getWinPercentage() {
        if (!hasPlayedGames()) {
            return 0;
        }
        return (int) Math.round(wins * 100.0 / getGamesPlayed());
    }

    public boolean hasId(int id) {
        return this.id == id;
    }

    public boolean usernameContains(String text) {
        return username.toLowerCase().contains(text.toLowerCase());
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    // Private helpers
    private boolean hasPlayedGames() {
        return getGamesPlayed() > 0;
    }

    // Validation
    private static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be a positive number, but was " + id);
        }
    }

    private static void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "Username must be 3-12 characters and only contain letters, digits or _, but was '" + username + "'");
        }
    }

    private static void validateCreationDate(LocalDate creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Creation date is required");
        }
        if (creationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Creation date cannot be in the future, but was " + creationDate);
        }
    }

    private static void validateStatistic(String name, int value) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative, but was " + value);
        }
    }
}