package org.chess.controller.dtoResponse;

// Record: tells the front end who is logged in (username is null when nobody is)
public record AccountResponse(String username) {
}