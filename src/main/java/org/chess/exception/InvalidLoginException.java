package org.chess.exception;

public class InvalidLoginException extends RuntimeException {

  // constructor
  public InvalidLoginException() {
    super("Wrong username or password");
  }
}