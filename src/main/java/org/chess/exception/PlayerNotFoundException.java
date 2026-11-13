package org.chess.exception;

public class PlayerNotFoundException extends RuntimeException {

    // constructor: not found by id
    public PlayerNotFoundException(int id) {
        super("Player was not found");
    }

    // constructor: not found by username
    public PlayerNotFoundException(String username) {
        super("Player was not found");
    }
}