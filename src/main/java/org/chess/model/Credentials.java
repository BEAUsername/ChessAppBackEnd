package org.chess.model;

// Record: the two values needed to check a login. Never sent to the front end.
public record Credentials(String username, String passwordHash) {
}