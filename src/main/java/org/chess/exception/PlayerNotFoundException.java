package org.chess.exception;

public class PlayerNotFoundException extends RuntimeException {

    // constructor
    public PlayerNotFoundException(int id) {
        super("Player with id" + id + "was not found");
    }
}
